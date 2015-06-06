package RandomPvP.Core.Server.Game;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Event.Game.GameStateChangeEvent;
import RandomPvP.Core.Event.Game.GamemodeChangeEvent;
import RandomPvP.Core.Event.Game.MapChangeEvent;
import RandomPvP.Core.Server.Game.GameState.GameState;
import RandomPvP.Core.Server.Game.Map.RMap;
import RandomPvP.Core.Server.Game.Mode.Gamemode;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.MotdData.MotdSetter;
import RandomPvP.Core.Server.General.ServerToggles;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    private static Game game;
    private static GameState state;
    private static Gamemode mode;
    private static RMap map;

    public static void setGame(Game g) {
        game = g;
        g.setupGame();
    }
    public static Game getGame() {
        return game;
    }

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

    public static int getID() {
        return Integer.valueOf(StringUtil.removeDisallowedCharacters("1234567890".toCharArray(), Bukkit.getServerName()));
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
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `servers_" + getGame().getName().toLowerCase() + "` WHERE server_id=?");
                        stmt.setInt(1, getID());
                        ResultSet res = stmt.executeQuery();
                        if(res.next()) {
                            PreparedStatement update = MySQL.getConnection().prepareStatement("UPDATE `servers_" + getGame().getName().toLowerCase() + "` SET ip=?, port=? WHERE server_id=?");
                            update.setString(1, Bukkit.getServer().getIp());
                            update.setInt(2, Bukkit.getServer().getPort());
                            update.setInt(3, getID());
                            update.executeUpdate();
                            return;
                        }
                    }
                    {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `servers_" + getGame().getName().toLowerCase() + "` VALUES (?, ?, ?)");
                        stmt.setInt(1, getID());
                        stmt.setString(2, Bukkit.getServer().getIp());
                        stmt.setInt(3, Bukkit.getServer().getPort());

                        stmt.executeUpdate();
                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
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
