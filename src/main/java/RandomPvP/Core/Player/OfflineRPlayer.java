package RandomPvP.Core.Player;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Player.UUID.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
public class OfflineRPlayer {

    boolean isnull = true;
    String name;
    UUID uuid;
    String ip = null;
    Rank rank = Rank.PLAYER;
    int credits = 0;
    int rpid;


    public OfflineRPlayer(final String name) {
        uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

        this.name = name;

        if (uuid == null) {
            isnull = true;
        } else {
            try {
                PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `accounts`.`uuid` = ?;");
                stmt.setString(1, getUUID().toString());
                ResultSet set = stmt.executeQuery();

                if (set.next()) {
                    setRank(Rank.valueOf(set.getString("rank").toUpperCase()));
                    setCredits(set.getInt("credits"));
                    ip = set.getString("ip").replace("-", ".");
                    setRPID(set.getInt("rpid"));
                    isnull = false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public OfflineRPlayer(final int rpid) {
        try {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `rpid`=?");
            stmt.setInt(1, rpid);
            ResultSet set = stmt.executeQuery();

            if (set.next()) {
                setRank(Rank.valueOf(set.getString("rank").toUpperCase()));
                setCredits(set.getInt("credits"));
                ip = set.getString("ip").replace("-", ".");
                setName(set.getString("username"));
                isnull = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

        if (uuid == null) {
            isnull = true;
        }
    }


    public synchronized void saveData() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `accounts` SET `username`=?, `rank`=?, `credits`=? WHERE `uuid`=?;");
                    stmt.setString(1, getName());
                    stmt.setString(2, getRank().getRank());
                    stmt.setInt(3, getCredits());
                    stmt.setString(4, getUUID().toString());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    public boolean isNull() {
        return isnull;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public boolean isBanned() {
        return PunishmentManager.getInstance().hasBanActive(getUUID());
    }

    public boolean isMuted() {
        return PunishmentManager.getInstance().hasMuteActive(getUUID());
    }

    public Punishment getMute() {
        return PunishmentManager.getInstance().getActiveMute(getUUID());
    }

    public String getRankedName(boolean hasTag) {
        if (hasTag) {
            return getRank().getTag() + getRank().getColor() + name;
        } else {
            return getRank().getColor() + name;
        }
    }

    public void setUUID(UUID id) {
        this.uuid = id;
    }
    public UUID getUUID() {
        return uuid;
    }

    public void setRPID(int id) {
        this.rpid = id;
    }
    public int getRPID() {
        return rpid;
    }
    public String getIP() {
        return ip;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
        saveData();
    }

    public Rank getRank() {
        return rank;
    }
    public boolean isStaff() {
        return (getRank().has(Rank.MOD));
    }
    public boolean isVIP() {
        return (getRank().has(Rank.VIP));
    }
    public boolean isDonator() {
        return (getRank().has(Rank.PREMIUM));
    }

    public void addCredits(int credits) {
        this.credits = getCredits() + credits;
    }
    public void removeCredits(int credits) {
        this.credits = getCredits() - credits;
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public int getCredits() {
        return credits;
    }

}
