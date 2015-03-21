package RandomPvP.Core.Player;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Game.Team.Team;
import RandomPvP.Core.Player.Counter.Counter;
import RandomPvP.Core.Player.Credit.Booster;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.Scoreboard.RandomPvPScoreboard;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Punish.PunishmentType;
import RandomPvP.Core.Punish.Warning;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Module.IModule;
import RandomPvP.Core.Util.NumberUtil;
import RandomPvP.Core.Util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    Player player = null;
    String name = null;
    UUID uuid = null;
    int rpid = nextId;
    String ip = "0.0.0.0";
    Rank rank = Rank.PLAYER;
    long rank_updated;
    int credits = 0;
    int killstreak = 0;
    Team team = null;
    HashMap<String, Counter> counters = new HashMap<>();
    HashMap<String, IModule> modules = new HashMap<>();
    boolean canInteract = false;
    RandomPvPScoreboard board = null;
    Booster booster = null;
    RStaff staff = null;
    boolean hasWarningPending = false;
    boolean ranked = false;
    int warnings = 0;
    boolean frozen = false;

    public RPlayer(String name, UUID id, boolean rankOnly) {
        setUUID(id);
        this.name = name;

        if(id != null) {
            if (RPICore.debugEnabled) {
                System.out.println(name + " has the UUID " + id.toString());
            }

            try {
                if (rankOnly) {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `rank` FROM `accounts` WHERE `accounts`.`uuid` = ?;");
                    stmt.setString(1, getUUID().toString());
                    ResultSet set = stmt.executeQuery();

                    if (set.next()) {
                        setRank(Rank.valueOf(set.getString("rank").toUpperCase()), false);
                    }
                } else {
                    {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `accounts`.`uuid` = ?;");
                        stmt.setString(1, getUUID().toString());
                        ResultSet set = stmt.executeQuery();

                        if (set.next()) {
                            setRank(Rank.valueOf(set.getString("rank").toUpperCase()), false);
                            rank_updated = System.currentTimeMillis();
                            setCredits(set.getInt("credits"));
                            setRPID(set.getInt("rpid"));
                        } else {
                            String sqlGetNextId = "SELECT MAX(rpid) AS max FROM accounts";
                            PreparedStatement statement;
                            statement = MySQL.getConnection().prepareStatement(sqlGetNextId);
                            ResultSet rs = statement.executeQuery();
                            while (rs.next()) {
                                nextId = rs.getInt("max") + 1;
                            }

                            PreparedStatement stmt2 = MySQL.getConnection().prepareStatement("INSERT INTO `accounts` VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                            stmt2.setString(1, getUUID().toString());
                            stmt2.setString(2, getName());
                            stmt2.setInt(3, 0);
                            stmt2.setString(4, getRank().getRank());
                            stmt2.setLong(5, rank_updated);
                            stmt2.setInt(6, getCredits());
                            stmt2.setString(7, getIP().replace(".", "-"));
                            stmt2.setString(8, null);

                            stmt2.executeUpdate();
                        }
                    }
                    {
                        //to slow down the time so the removal in between switching servers doesn't overlap
                        new BukkitRunnable() {
                            public void run() {
                                try {
                                    PreparedStatement statement = MySQL.getConnection().prepareStatement("INSERT INTO `online_players` VALUES (?, ?, ?);");
                                    statement.setInt(1, getRPID());
                                    statement.setString(2, getName());
                                    statement.setString(3, Bukkit.getServerName());
                                    statement.executeUpdate();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }.runTaskLaterAsynchronously(RPICore.getInstance(), 25L);


                        PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT * FROM `credit_booster` WHERE `rpid` = ?");
                        statement.setInt(1, getRPID());
                        ResultSet rs = statement.executeQuery();
                        if (rs.next()) {
                            Booster boost = new Booster(this, rs.getLong("duration"), rs.getInt("multiplier"));
                            setBooster(boost);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void saveData() {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `accounts` SET `username`=?, `rank`=?, `rank_updated`=?, `credits`=?, `ip` =? WHERE `uuid`=?;");
                    stmt.setString(1, getName());
                    stmt.setString(2, getRank().getRank());
                    stmt.setLong(3, rank_updated);
                    stmt.setInt(4, getCredits());
                    stmt.setString(5, getIP().replace(".", "-"));
                    stmt.setString(6, getUUID().toString());
                    stmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setPlayer(Player player) {
        if (player != null && player.isOnline()) {
            this.player = player;
            this.name = player.getName();

            String ipAddress = player.getAddress().toString();

            try {
                if ((ipAddress.charAt(0) == '/') || (ipAddress.charAt(0) == '\\'))
                    ipAddress = ipAddress.substring(1);
            } catch (Exception e) {
                System.out.println("Error with given IP: " + ipAddress);
            }

            try {
                if ((ipAddress.contains(":")) && (ipAddress.indexOf(':') != -1))
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(':'));
            } catch (Exception e) {
                System.out.println("Error with given IP: " + ipAddress);
            }

            setIP(ipAddress);
        }

    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName(boolean hasTag) {
        //CONFUSING

        // if has tag
        if (hasTag) {
            // if team sets color and has tag
            if (getTeam() != null && getTeam().setsTeamDisplayNameColor()) {
                return getRank().getTag() + getTeam().getColor() + name;
            } else {
                // if team doesn't and has tag
                return getRankedName(true);
            }
        } else {
            // if team sets color but doesn't have tag
            if (getTeam() != null && getTeam().setsTeamDisplayNameColor()) {
                return getTeam().getColor() + name;
            } else {
                return getRankedName(false);
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

    public void setRank(final Rank rank, boolean saveData) {
        if (rank != getRank()) {

            this.rank = rank;
            rank_updated = System.currentTimeMillis();
            if (saveData) {
                saveData();
            }

            if (has(Rank.MOD)) {
                staff = new RStaff(this);
            }

            new BukkitRunnable() {
                public void run() {
                    RandomPvPScoreboard.updateScoreboard(true);
                }
            }.runTaskLater(RPICore.getInstance(), 5L);


        }
    }

    public Rank getRank() {
        return rank;
    }

    public RStaff getStaff() {
        if (has(Rank.MOD)) {
            return staff;
        }

        return null;
    }

    public boolean has(Rank rank) {
        return getRank().has(rank);
    }

    public int getWarnings() {
        return warnings;
    }

    public void addWarning() {
        this.warnings = getWarnings() + 1;
        setHasWarningPending(true);
    }

    public boolean isBanned() {
        return PunishmentManager.getInstance().hasBanActive(getUUID());
    }

    public boolean isMuted() {
        return PunishmentManager.getInstance().hasMuteActive(getUUID());
    }

    public Punishment getMute() {
        return PunishmentManager.getInstance().getActiveMute(getUUID());
    }

    public boolean hasWarningPending() {
        return hasWarningPending;
    }

    public void setHasWarningPending(boolean warningPending) {
        this.hasWarningPending = warningPending;
    }

    public void warn(String type, String message) {
        message("\n");
        message("  §4§lWarning §8|| §c" + type);
        message("  §7" + message);
        message("  §cPlease type §8[ §7/rdpvp §8] §cto acknowledge this warning.");
        message("\n");

        if (hasWarningPending || getWarnings() >= 1) {
            getPlayer().kickPlayer((message));
        } else {
            if (RPICore.pEnabled) {
                Punishment punish = new Punishment(
                        getUUID(),
                        null,
                        PunishmentType.WARN,
                        type,
                        TimeUtil.dateFormat.format(new Date()),
                        0L);

                punish.save();
            }
            addWarning();
            setFrozen(true);
        }
    }

    public void warn(Warning type) {
        warn(type.getName(), type.getDescription());
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean freeze) {
        frozen = freeze;
    }

    public void addCredits(int credits) {
        String reason = "";
        int credAdded = credits;
        switch (getRank()) {
            case PLAYER:
                break;
            case PRIME:
                credAdded = credits * 2;
                reason = " §6§lx2 Prime Bonus";
                break;
            case PREMIUM:
                credAdded = credits * 3;
                reason = " §b§lx3 Premium Bonus";
                break;
            case VIP:
                credAdded = credits * 3;
                reason = " §e§lx3 VIP Bonus";
                break;
            case BUILDER:
                credAdded = credits * 3;
                reason = " §2§lx3 Builder Bonus";
                break;
            case MOD:
                credAdded = credits * 5;
                reason = " §5§lx5 Staff Bonus";
                break;
            case ADMIN:
                credAdded = credits * 5;
                reason = " §5§lx5 Staff Bonus";
                break;
            case OWNER:
                credAdded = credits * 10;
                reason = " §4§lx10 Owner Bonus";
                break;
        }

        if (getBooster() != null) {
            if (getBooster().isActive()) {
                credAdded = credAdded * getBooster().getMultiplier();
                reason = reason + " §1§lx" + getBooster().getMultiplier() + " Credit Booster";
            } else {
                getBooster().deactivate();
                message(MsgType.INFO, "Your 1 day credit booster has now ended.");
            }
        }

        this.credits = getCredits() + credAdded;

        if (!reason.equalsIgnoreCase("")) {
            message(MsgType.CREDIT, "§a+" + credAdded + " Credits!" + reason);
        } else {
            message(MsgType.CREDIT, "§a+" + credAdded + " Credits!");
        }
    }

    public void removeCredits(int credits) {
        this.credits = getCredits() - credits;
        message(MsgType.GAME, "§c-" + credits + " Credits!");
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCredits() {
        return credits;
    }

    public void setBooster(Booster boost) {
        this.booster = boost;
    }

    public Booster getBooster() {
        return booster;
    }

    public boolean hasActiveBooster() {
        return getBooster() != null;
    }

    public void addCounter(Counter counter) {
        counters.put(counter.getName(), counter);
    }

    public Counter getCounter(String name) {
        for (String counter : counters.keySet()) {
            if (name.equalsIgnoreCase(counter)) {
                return counters.get(counter);
            }
        }

        return null;
    }

    public Map<String, Counter> getCounters() {
        return counters;
    }

    public void removeCounter(Counter counter) {
        if (counters.containsKey(counter.getName())) {
            counters.remove(counter.getName());
        }
    }

    public void addModule(IModule module) {
        modules.put(module.getName(), module);
    }

    public IModule getModule(String name) {
        for (String module : modules.keySet()) {
            if (name.equalsIgnoreCase(module)) {
                return modules.get(module);
            }
        }

        return null;
    }

    public Map<String, IModule> getModules() {
        return modules;
    }

    public void removeCounter(IModule module) {
        if (modules.containsKey(module.getName())) {
            modules.remove(module.getName());
        }
    }

    public void setKillstreak(int streak) {
        this.killstreak = streak;
    }

    public int getKillStreak() {
        return killstreak;
    }

    public void addKill() {
        setKillstreak(getKillStreak() + 1);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setRanked(boolean bln) {
        ranked = bln;
    }

    public boolean isRanked() {
        return ranked;
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

    public String getHealth() {
        return "" + NumberUtil.trimNumber((double) getPlayer().getHealth());
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public void setAllowFlight(boolean flight) {
        getPlayer().setAllowFlight(flight);
    }

    public boolean isFlightAllowed() {
        return getPlayer().getAllowFlight();
    }

    public void teleport(Location loc) {
        getPlayer().teleport(loc);
    }

    public boolean isOnline() {
        return getPlayer().isOnline();
    }

    public void setCompassTarget(Location loc) {
        getPlayer().setCompassTarget(loc);
    }

    public void setCompassTarget(RPlayer player) {
        while (player != null && player.getPlayer() != null) {
            setCompassTarget(player.getLocation());
        }
    }

    public Location getCompassTarget() {
        return getPlayer().getCompassTarget();
    }

    public GameMode getGameMode() {
        return getPlayer().getGameMode();
    }

    RPlayer last_damage = null;

    public void setPlayerLastHitBy(RPlayer player) {
        last_damage = player;
    }

    public RPlayer getPlayerLastHitBy() {
        return last_damage;
    }

    public void message(String message) {
        player.sendMessage(message);
    }

    public void message(MsgType type, String message) {
        player.sendMessage(type.getPrefix() + message);
    }

    public RandomPvPScoreboard getScoreboard() {
        return board;
    }

    public void setScoreboard(RandomPvPScoreboard scoreboard) {
        board = scoreboard;
    }

    public void send(String server) {
        message(MsgType.NETWORK, "§aConnecting you to §b" + server.toUpperCase() + "§a...");

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server.toUpperCase());
            getPlayer().sendPluginMessage(RPICore.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            message(ChatColor.RED + "Cannot connect you to the specified server at this time");
        }
    }

}
