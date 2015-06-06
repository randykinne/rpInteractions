package RandomPvP.Core.Util;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Server.General.ServerToggles;
import RandomPvP.Core.Util.MotdData.MotdFetcher;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class NetworkUtil {

    public static ArrayList<String> getOnlineStaff() {
        ArrayList<String> staff = new ArrayList<>();
        {
            for(String s : getOnlinePlayers()) {
                if(new OfflineRPlayer(s).isStaff()) {
                    staff.add(s);
                }
            }
        }
        return staff;
    }

    @Deprecated
    public static void handleErrorMessage(final RPlayer p, final Exception ex) {
        p.message(MsgType.ERROR, "Whoops, there was an error.");

        handleError(ex);
    }

    public static void handleError(RPlayer pl, Exception ex) {
        pl.message(MsgType.ERROR, "Whoops, there was an error.");

        handleError(ex);
    }

    public static void handleError(final Exception ex) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement check = MySQL.getConnection().prepareStatement("SELECT * FROM `errors` WHERE `error`=? && `cause`=? && `server`=?");
                    {
                        check.setString(1, ex.getClass().getSimpleName());
                        check.setString(2, ex.getStackTrace()[0].getClassName() + ":" + ex.getStackTrace()[0].getLineNumber());
                        check.setString(3, GameManager.getGame().getName()+GameManager.getID());
                    }
                    ResultSet cRes = check.executeQuery();

                    if(cRes.next()) {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `errors` SET `occurrences`=?,`fixed`=? WHERE `error`=? && `cause`=? && `server`=?");
                        {
                            stmt.setInt(1, cRes.getInt("occurrences")+1);
                            stmt.setBoolean(2, false);
                            stmt.setString(3, ex.getClass().getSimpleName());
                            stmt.setString(4, ex.getStackTrace()[0].getClassName() + ":" + ex.getStackTrace()[0].getLineNumber());
                            stmt.setString(5, GameManager.getGame().getName()+GameManager.getID());
                        }
                        stmt.executeUpdate();
                    } else {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `errors` VALUES (?,?,?,?,?);");
                        {
                            stmt.setString(1, ex.getClass().getSimpleName());
                            stmt.setString(2, ex.getStackTrace()[0].getClassName() + ":" + ex.getStackTrace()[0].getLineNumber());
                            stmt.setString(3, GameManager.getGame().getName()+GameManager.getID());
                            stmt.setInt(4, 1);
                            stmt.setBoolean(5, false);
                        }
                        stmt.executeUpdate();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); //lol
                }
            }
        }.start();

        if(!ServerToggles.isShowErrors() && !RPICore.debugEnabled) {
            System.out.println("*********************************************");
            System.out.println("*CHECK http://rperrors.comuf.com/errors.html*");
            System.out.println("*                  ERROR                    *");
            System.out.println("*********************************************");
        } else {
            ex.printStackTrace();
        }
    }

    public static String getCurrentServer(final OfflineRPlayer p) {
        try {
            ExecutorService s = Executors.newCachedThreadPool();
            Future<String> task = s.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `server` FROM `online_players` WHERE `id`=?");
                    {
                        stmt.setInt(1, p.getRPID());
                    }
                    ResultSet res = stmt.executeQuery();
                    if (res.next()) {
                        return res.getString("server");
                    }

                    return null;
                }
            });
            return task.get();
        } catch (Exception ex) {
            handleError(ex);
            return null;
        }
    }

    public static ArrayList<String> getOnlinePlayers() {
        try {
            ArrayList<String> data = new ArrayList<>();
            ExecutorService s = Executors.newCachedThreadPool();
            Future<ArrayList<String>> task = s.submit(new Callable<ArrayList<String>>() {
                @Override
                public ArrayList<String> call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `username` FROM `online_players`;");
                    ResultSet res = stmt.executeQuery();
                    ArrayList<String> data = new ArrayList<>();
                    {
                        while (res.next()) {
                            data.add(res.getString("username"));
                        }
                    }
                    return data;
                }
            });
            return task.get();
        } catch (Exception ex) {
            handleError(ex);
            return null;
        }
    }

    public static String[] convertServerData(MotdFetcher fetcher) {
        return fetcher.getMotd().split("\\|");
    }

    public static ResultSet getServerAddress(final String gamemode, final int num) {
        try {
            Future<ResultSet> task = Executors.newCachedThreadPool().submit(new Callable<ResultSet>() {
                @Override
                public ResultSet call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `servers_" + gamemode + "` WHERE `server_id`=?");
                    stmt.setInt(1, num);
                    return stmt.executeQuery();
                }
            });
            return task.get();
        } catch (Exception ex) {
            handleError(ex);
            return null;
        }
    }

    public static void restart() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                List<String> args = ManagementFactory.getRuntimeMXBean()
                        .getInputArguments();
                List<String> command = new ArrayList<String>();
                command.add(System.getProperty("java.home") + File.separator
                        + "bin" + File.separator + "java.exe");
                for (int i = 0; i < args.size(); i++) {
                    command.add(args.get(i));
                }
                command.add("-jar");
                command.add(new File(Bukkit.class.getProtectionDomain()
                        .getCodeSource().getLocation().getFile())
                        .getAbsolutePath());
                try {
                    new ProcessBuilder(command).start();
                } catch (Exception ex) {
                    handleError(ex);
                }
            }
        });
        Bukkit.shutdown();

    }

}
