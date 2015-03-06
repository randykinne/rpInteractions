package RandomPvP.Core.Game;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Event.Game.GameStateChangeEvent;
import RandomPvP.Core.Event.Game.GamemodeChangeEvent;
import RandomPvP.Core.Event.Game.MapChangeEvent;
import RandomPvP.Core.Game.GameState.GameState;
import RandomPvP.Core.Game.Map.RMap;
import RandomPvP.Core.Game.Mode.Gamemode;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.MotdData.MotdSetter;
import RandomPvP.Core.Util.NumberUtil;
import RandomPvP.Core.Util.ServerToggles;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class GameManager {

    static Game game;
    static GameState state;
    static Gamemode mode;
    static RMap map;
    static long gameStart;
    static long gameEnd;

    public static void setGame(Game gam) {
        game = gam;
    }
    public static Game getGame() {
        return game;
    }

    public static void setGameStart(long start) { gameStart = start; }
    public static void setGameEnd(long end) { gameEnd = end; }

    public static long getGameLength() { return gameStart - System.currentTimeMillis(); }
    public static long getGameStart() { return gameStart; }
    public static long getGameEnd() { return gameEnd; }

    public static RMap getMap() {
        return map;
    }
    public static void setMap(RMap world) {
        if (map != world) {
            if (map != null) {
                Bukkit.getPluginManager().callEvent(new MapChangeEvent(world, map));
            }
            map = world;
            updateMotd();
        }
    }

    public static void setMode(Gamemode m) {
        if (m != mode) {
            if (m != null) {
                Bukkit.getPluginManager().callEvent(new GamemodeChangeEvent(m));
            }
            mode = m;
            updateMotd();
        }
    }
    public static Gamemode getMode() { return mode; }

    public static void setState(GameState state1) {
        if (state != state1) {
            if (state != null) {
                Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(state1));
            }
            state = state1;
            updateMotd();
        }

    }
    public static GameState getState() {
        return state;
    }

    public boolean isState(GameState compare) {
        return state == compare;
    }

    public static void loadServerProfile() {
        new BukkitRunnable() {
            public void run() {
                try {
                    {
                        PreparedStatement stmt= MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `servers_" + getGame().getName().toLowerCase() + "` (" +
                                "  `server_id` INT NOT NULL," +
                                "  `ip` VARCHAR(255) NOT NULL," +
                                "  `port` INT NOT NULL);");

                        stmt.executeUpdate();
                    }
                    {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `servers_" + getGame().getName().toLowerCase() + "` WHERE server_id=?;");
                        stmt.setInt(1, getGame().getID());
                        ResultSet res = stmt.executeQuery();
                        if(res.next()) {
                            PreparedStatement update = MySQL.getConnection().prepareStatement("UPDATE `servers_" + getGame().getName().toLowerCase() + "` SET ip=?, port=? WHERE server_id=?;");
                            stmt.setString(1, Bukkit.getServer().getIp());
                            stmt.setInt(2, Bukkit.getServer().getPort());
                            update.setInt(3, getGame().getID());
                            update.executeUpdate();
                            return;
                        }
                    }
                    {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `servers_" + getGame().getName().toLowerCase() + "` VALUES (?, ?, ?);");
                        stmt.setInt(1, getGame().getID());
                        stmt.setString(2, Bukkit.getServer().getIp());
                        stmt.setInt(3, Bukkit.getServer().getPort());

                        stmt.executeUpdate();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    public static void updateMotd() {
        try {
            new MotdSetter().setMotd(getMap().getName() + "|" + getState().getName() + "|" + getMode().getName() + "|" + ServerToggles.getRankRequired().toString());
        } catch (NullPointerException ignored) {} //on startup
    }

}
