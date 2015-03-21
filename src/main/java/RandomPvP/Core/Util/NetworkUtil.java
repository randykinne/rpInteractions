package RandomPvP.Core.Util;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.MotdData.MotdFetcher;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static String[] getOnlineStaff() {
        final StringBuilder sb = new StringBuilder();
        new BukkitRunnable() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `online_players`;");
                    ResultSet res = stmt.executeQuery();
                    while (res.next()) {
                        if (new OfflineRPlayer(res.getString("username")).getRank().has(Rank.MOD)) {
                            sb.append(res.getString("username") + "|");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
        return sb.toString().split("\\|");
    }

    public static String getCurrentServer(final OfflineRPlayer p) {
        try {
            ExecutorService s = Executors.newCachedThreadPool();
            Future<String> task = s.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `server` FROM `online_players` WHERE `id`=?;");
                    {
                        stmt.setInt(1, p.getRPID());
                    }
                    ResultSet res = stmt.executeQuery();
                    while (res.next()) {
                        return res.getString("server");
                    }
                    return null;
                }
            });
            return task.get();
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Bukkit.shutdown();
    }

}
