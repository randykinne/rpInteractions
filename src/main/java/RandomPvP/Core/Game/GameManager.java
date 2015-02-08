package RandomPvP.Core.Game;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Event.Game.GameStateChangeEvent;
import RandomPvP.Core.Event.Game.GamemodeChangeEvent;
import RandomPvP.Core.Event.Game.MapChangeEvent;
import RandomPvP.Core.Game.GameState.GameState;
import RandomPvP.Core.Game.Map.RMap;
import RandomPvP.Core.Game.Mode.Gamemode;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.NumberUtil;
import RandomPvP.Core.Util.ServerToggles;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
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

    public static void setGame(Game gam) { game = gam;}
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
        }
    }

    public static void setMode(Gamemode m) {
        if (m != mode) {
            if (m != null) {
                Bukkit.getPluginManager().callEvent(new GamemodeChangeEvent(m));
            }
            mode = m;
        }
    }
    public static Gamemode getMode() { return mode; }

    public static void setState(GameState state1) {
        if (state != state1) {
            if (state != null) {
                Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(state1));
            }
            state = state1;
        }

    }
    public static GameState getState() {
        return state;
    }

    public boolean isState(GameState compare) {
        return state == compare;
    }

    public static void loadGame() throws SQLException {

        {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `servers_" + getGame().getName().toLowerCase() + "` (" +
                            "  `server_id` INT NOT NULL," +
                            "  `map_name` VARCHAR(255) NOT NULL," +
                            "  `game_state` VARCHAR(255) NOT NULL," +
                            "  `game_mode` VARCHAR(255) NOT NULL," +
                            "  `time` BIGINT(8) NOT NULL," +
                            "  `rank_whitelist` VARCHAR(255) NOT NULL);");

            stmt.executeUpdate();
        }

        {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `servers_" + getGame().getName().toLowerCase() + "` VALUES (?, ?, ?, ?, ?, ?);");
            stmt.setInt(1, getGame().getID());
            stmt.setString(2, getMap().getName());
            stmt.setString(3, getState().getName());
            stmt.setString(4, getMode().getColor() + getMode().getName());
            stmt.setLong(5, 0L);
            stmt.setString(6, ServerToggles.getRankRequired().getRank());

            stmt.executeUpdate();
        }
    }

    public static void unloadGame() throws SQLException {
        PreparedStatement stmt = MySQL.getConnection().prepareStatement("DELETE FROM `19359`.`servers_" + getGame().getName().toLowerCase() + "` WHERE `server_id` = ?");
        stmt.setInt(1, getGame().getID());

        stmt.executeUpdate();
    }

    public static void updateGame() {
        new BukkitRunnable() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `servers_" + getGame().getName().toLowerCase() + "` SET `map_name`=?, `game_state`=?, `game_mode`=?, `time`=?, `rank_whitelist`=? WHERE `server_id`=?;");
                    stmt.setString(1, getMap().getName());
                    stmt.setString(2, getState().getName());
                    stmt.setString(3, getMode().getColor() + getMode().getName());
                    stmt.setLong(4, 0L);
                    stmt.setString(5, ServerToggles.getRankRequired().getRank());
                    stmt.setInt(6, getGame().getID());

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }
}
