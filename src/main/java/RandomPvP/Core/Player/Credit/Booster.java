package RandomPvP.Core.Player.Credit;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.NumberUtil;
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
public class Booster {

    RPlayer player;
    long duration;
    int level;
    int id;

    public Booster(RPlayer player, long duration, int level) {
        this.player = player;
        this.duration = duration;
        this.level = level;
    }

    public RPlayer getPlayer() { return player; }

    public void setPlayer(RPlayer player) { this.player = player; }

    public void activate() {
        new BukkitRunnable() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `credit_booster` VALUES (?, ?, ?, ?, ?);");
                    stmt.setInt(1, 0);
                    stmt.setInt(2, getPlayer().getRPID());
                    stmt.setInt(3, getMultiplier());
                    stmt.setLong(4, getDuration());
                    stmt.setBoolean(5, isActive());

                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    public void deactivate() {
        new BukkitRunnable() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `credit_booster` SET `active`=? WHERE `id` = ?;");
                    stmt.setBoolean(1, isActive());
                    stmt.setInt(2, getID());

                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public long getDuration() { return duration; }

    public String getReadableDuration() { return NumberUtil.translateDuration(duration); }

    public void setDuration(long duration) { this.duration = duration; }

    public int getMultiplier() { return level; }

    public void setMultiplier(int level) { this.level = level; }

    public boolean isActive() { return getDuration() - System.currentTimeMillis() > 0L; }
}
