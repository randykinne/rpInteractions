package RandomPvP.Core.Player;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Game.Team.Team;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.Scoreboard.RandomPvPScoreboard;
import RandomPvP.Core.RPICore;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
public class RPlayer {

    private int nextId = 1;
    boolean loaded = false;
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    public boolean isLoaded() {
        return loaded;
    }
    Player player;
    String name;
    String display_name;
    UUID uuid;
    int rpid = nextId;
    String ip = "0.0.0.0";
    Rank rank = Rank.PLAYER;
    int credits = 0;
    Team team;
    boolean canInteract = false;
    RandomPvPScoreboard board;
    boolean hasSTFUEnabled = false;
    boolean hasWarningPending = false;
    int warnings = 0;


    public RPlayer(String name, UUID id) {
        setUUID(id);
        this.name = name;

        try {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `accounts`.`uuid` = ?;");
            stmt.setString(1, getUUID().toString());
            ResultSet set = stmt.executeQuery();

            if (set.next()) {
                setRank(Rank.valueOf(set.getString("rank").toUpperCase()), false);
                setCredits(set.getInt("credits"));
                setRPID(set.getInt("rpid"));
                /*if (!getIP().equalsIgnoreCase(set.getString("ip"))) {
                    if (isStaff()) {
                        RPStaff.questionIP(RPlayerManager.getInstance().getPlayer(getPlayer()));
                    } else {
                        saveData();
                    }
                }*/
            } else {
                String sqlGetNextId = "SELECT MAX(rpid) AS max FROM accounts";
                PreparedStatement statement;
                statement = MySQL.getConnection().prepareStatement(sqlGetNextId);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    nextId = rs.getInt("max") + 1;
                }

                PreparedStatement stmt2 = MySQL.getConnection().prepareStatement("INSERT INTO `accounts` VALUES (?, ?, ?, ?, ?, ?, ?);");
                stmt2.setString(1, getUUID().toString());
                stmt2.setString(2, getName());
                stmt2.setInt(3, 0);
                stmt2.setString(4, getRank().getRank());
                stmt2.setInt(5, getCredits());
                stmt2.setString(6, getIP().replace(".", "-"));
                stmt2.setString(7, null);

                stmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveData() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `accounts` SET `username`=?, `rank`=?, `credits`=?, `ip` =? WHERE `uuid`=?;");
                    stmt.setString(1, getName());
                    stmt.setString(2, getRank().getRank());
                    stmt.setInt(3, getCredits());
                    stmt.setString(4, getIP().replace(".", "-"));
                    stmt.setString(5, getUUID().toString());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(RPICore.getInstance());
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.name = player.getName();
        this.display_name = player.getDisplayName();
        setIP(player.getAddress().toString().replace("/", ""));

    }
    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName(boolean hasTag) {
        // if has tag
        if (hasTag) {
            // if team sets display name and has tag
            if (getTeam() != null && getTeam().setsTeamDisplayNameColor()) {
                return getRank().getTag() + getTeam().getColor() + display_name;
            } else {
                // if team doesn't and has tag
                return getRank().getTag() + ChatColor.GRAY + display_name;
            }
        } else {
            // if team sets color but doesn't have tag
            if (getTeam() != null && getTeam().setsTeamDisplayNameColor()) {
                return getTeam().getColor() + display_name;
            } else {
                return ChatColor.GRAY+ display_name;
            }
        }
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

    public void setIP(String ip) {
        this.ip = ip;
    }
    public String getIP() {
        return ip;
    }

    public void setRank(Rank rank, boolean saveData) {
        this.rank = rank;
        if (saveData) {
            saveData();
        }
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

    public int getWarnings() {
        return warnings;
    }

    public void addWarning() {
        this.warnings = getWarnings() + 1;
        setHasWarningPending(true);
    }

    public boolean hasWarningPending() { return hasWarningPending; }
    public void setHasWarningPending(boolean warningPending) { this.hasWarningPending = warningPending; }

    public void warn(String message) {
        message("§4╔══════════════════════════════════");
        message("§4║   §c§lWarning");
        message("§4║   §7§o" + message );
        message("§4║   §cPlease type§8 §8§l[ §7/rdpvp §8§l] §cto acknowledge this warning." );
        message("§4╚══════════════════════════════════");
        if (hasWarningPending || getWarnings() >= 1) {
            getPlayer().kickPlayer((message));
        } else {
            addWarning();
        }
    }

    public void toggleSTFU() {
        if (hasSTFUEnabled) {
            message("§6§l>> §eTurned §c§lOFF §estfu mode!");
            message("§6§l>> §6You can now see all staff messages.");
            getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_PLING, 2L, 1L);
            hasSTFUEnabled = false;
        } else {
            message("§6§l>> §eTurned §a§lON §estfu mode!");
            message("§6§l>> §6It'll automatically disable when you log off.");
            getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_PLING, 2L, 1L);
            hasSTFUEnabled = true;
        }
    }
    public boolean hasSTFUEnabled() {
        return hasSTFUEnabled;
    }

    public void addCredits(int credits, String reason) {
        this.credits = getCredits() + credits;
            message("§2§l>> §a§lCredits§8: §b" + credits + " §8- §7" + reason);
    }
    public void removeCredits(int credits, String reason) {
        this.credits = getCredits() - credits;
        message("§2§l>> §a§lCredits§8: §c" + credits + " §8- §7" + reason);
    }
    public void setCredits(int credits) {
        this.credits = credits;
    }
    public int getCredits() {
        return credits;
    }

    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean canInteract() {
        if (getTeam() != null) {
            return getTeam().getType() == Team.Type.Participating;
        }

        return canInteract;
    }

    public void setCanInteract(boolean interact) {
        this.canInteract = interact;
    }

    public void message(String message) {
        player.sendMessage(message);
    }

    public RandomPvPScoreboard getScoreboard() {
        return board;
    }
    public void setScoreboard(RandomPvPScoreboard scoreboard) {
        board = scoreboard;
    }

    private static ByteArrayOutputStream b = new ByteArrayOutputStream();
    private static DataOutputStream out = new DataOutputStream(b);

    public void send(String server) {
        if (server.equalsIgnoreCase("Hub")) {
            server = "H1";
        }
        System.out.println("Sending " + getName() + " to " + server);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
            getPlayer().sendPluginMessage(RPICore.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToHub(String message) {
        message("§4╔══════════════════════════════════");
        message("§4║   §4§lYou were disconnected from that server!");
        message("§4║   §d[§5§l!§d] §f§o" + message);
        message("§4╚══════════════════════════════════");
        new BukkitRunnable() {
            @Override
        public void run() {
                send("Hub");
            }
        }.runTaskLaterAsynchronously(RPICore.getInstance(), 20L);
    }

}
