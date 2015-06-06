package RandomPvP.Core.Player;

import RandomPvP.Core.Data.Json.JsonDataSet;
import RandomPvP.Core.Data.Json.RDatabaseWebCall;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.Player.Stats.Stats;
import RandomPvP.Core.Player.Toggles.PlayerToggles;
import RandomPvP.Core.Server.Game.Team.Team;
import RandomPvP.Core.Player.Counter.Counter;
import RandomPvP.Core.Player.Credit.Booster;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.Scoreboard.ScoreboardManager;
import RandomPvP.Core.Server.Game.Team.TeamManager;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Module.IModule;
import RandomPvP.Core.Util.Nametags.NametagAPI;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.NumberUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RPlayer {

    private Player player = null;
    private String name = null;
    private UUID uuid = null;
    private int rpid = 0;
    private String ip = "0.0.0.0";
    private Rank rank = Rank.PLAYER;
    private int credits = 0;
    private int killstreak = 0;
    private Team team = null;
    private Team lastTeam = null;
    private HashMap<String, Counter> counters = new HashMap<>();
    private HashMap<String, IModule> modules = new HashMap<>();
    private boolean canInteract = false;
    private Booster booster = null;
    private RStaff staff = null;
    private boolean ranked = false;
    private ChatColor namecolor = null;
    private Stats stats;
    private PlayerToggles toggles;

    public RPlayer(String name, final UUID id, boolean rankOnly) {
        setUUID(id);
        this.name = name;

        if(id != null) {
            if (RPICore.debugEnabled) {
                System.out.println(name + " has the UUID " + id.toString());
            }

            /*
            if (rankOnly) {
                JsonDataSet set = new RDatabaseWebCall("accounts").call();

                if(set.getWhere("uuid", id.toString()) != null) {
                    JSONObject obj = set.getWhere("uuid", id.toString());

                    setDataRank(Rank.valueOf((String) obj.get("rank")), false);
                }
            } else {
                {
                    JsonDataSet set = new RDatabaseWebCall("accounts").call();

                    if (set.getWhere("uuid", id.toString()) != null) {
                        JSONObject obj = set.getWhere("uuid", id.toString());

                        setDataRank(Rank.valueOf((String) obj.get("rank")), false);
                        setCredits(Integer.valueOf((String) obj.get("credits")));
                        setRPID(Integer.valueOf((String) obj.get("rpid")));
                    } else {
                        try {
                            PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `accounts` VALUES (?, ?, ?, ?, ?, ?);");
                            {
                                insert.setString(1, getUUID().toString());
                                insert.setString(2, getName());
                                insert.setInt(3, 0); //AUTO_INCREMENT
                                insert.setString(4, getRank().toString());
                                insert.setInt(5, getCredits());
                                insert.setString(6, getIP().replace(".", "-"));
                            }

                            insert.executeUpdate();
                        } catch (SQLException ex) {
                            NetworkUtil.handleError(ex);
                        }

                        //to get their rpid since it is auto increment
                        JSONObject obj = new RDatabaseWebCall("accounts").call().getWhere("uuid", id.toString());

                        setRPID(Integer.valueOf((String) obj.get("rpid")));
                    }
                }
                {
                    stats = new Stats(0, 0);
                    JsonDataSet set = new RDatabaseWebCall("stats").call();

                    if (set.getWhere("uuid", id.toString()) != null) {
                        JSONObject obj = set.getWhere("uuid", id.toString());

                        stats.setDeaths(Integer.valueOf((String) obj.get("deaths")));
                        stats.setKills(Integer.valueOf((String) obj.get("kills")));
                    } else {
                        try {
                            PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `stats` VALUES (?,?,?)");
                            {
                                insert.setString(1, getUUID().toString());
                                insert.setInt(2, 0);
                                insert.setInt(3, 0);
                            }
                            insert.executeUpdate();
                        } catch (SQLException ex) {
                            NetworkUtil.handleError(ex);
                        }
                    }
                }
                {
                    toggles = new PlayerToggles(false, false, false);
                    JsonDataSet set = new RDatabaseWebCall("toggles").call();

                    if (set.getWhere("rpid", getRPID() + "") != null) {
                        JSONObject obj = set.getWhere("rpid", getRPID() + "");

                        toggles.setChatAcknowledgements(Boolean.valueOf((String) obj.get("chatAcknowledgements")));
                        toggles.setPlayerMessages(Boolean.valueOf((String) obj.get("incognito")));
                        toggles.setIncognito(Boolean.valueOf((String) obj.get("playerMessages")));
                    } else {
                        try {
                            PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `toggles` VALUES (?,?,?,?)");
                            {
                                insert.setInt(1, getRPID());
                                insert.setBoolean(2, false);
                                insert.setBoolean(3, false);
                                insert.setBoolean(4, false);
                            }
                            insert.executeUpdate();
                        } catch (SQLException ex) {
                            NetworkUtil.handleError(ex);
                        }
                    }
                }
            }
            {
                //to slow down the time so the removal in between switching servers doesn't overlap
                new BukkitRunnable() {
                    public void run() {
                        try {
                            PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `online_players` VALUES (?,?,?)");
                            {
                                stmt.setInt(1, getRPID());
                                stmt.setString(2, getThis().getName());
                                stmt.setString(3, Bukkit.getServerName());
                            }
                            stmt.executeUpdate();
                        } catch (SQLException ex) {
                            NetworkUtil.handleError(ex);
                        }
                    }
                }.runTaskLater(RPICore.getInstance(), 30L);
            }
            */

            try {
                if (rankOnly) {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `rank` FROM `accounts` WHERE `uuid` = ?;");
                    stmt.setString(1, getUUID().toString());
                    ResultSet set = stmt.executeQuery();

                    if (set.next()) {
                        setDataRank(Rank.valueOf(set.getString("rank").toUpperCase()), false);
                    }
                } else {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `uuid` = ?;");
                    stmt.setString(1, getUUID().toString());
                    final ResultSet set = stmt.executeQuery();

                    if (set.next()) {
                        setDataRank(Rank.valueOf(set.getString("rank").toUpperCase()), false);
                        setCredits(set.getInt("credits"));
                        setRPID(set.getInt("rpid"));
                    } else {
                        PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `accounts` VALUES (?, ?, ?, ?, ?, ?);");

                        insert.setString(1, getUUID().toString());
                        insert.setString(2, getName());
                        insert.setInt(3, 0);
                        insert.setString(4, getRank().getRank());
                        insert.setInt(5, getCredits());
                        insert.setString(6, getIP().replace(".", "-"));

                        insert.executeUpdate();

                        PreparedStatement getRpid = MySQL.getConnection().prepareStatement("SELECT `rpid` FROM `accounts` WHERE `uuid`=?");

                        getRpid.setString(1, getUUID().toString());

                        rpid = getRpid.executeQuery().getInt("rpid");
                    }

                    new BukkitRunnable() {
                        public void run() {
                            try {
                                PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `online_players` VALUES (?,?,?)");
                                {
                                    stmt.setInt(1, getRPID());
                                    stmt.setString(2, getThis().getName());
                                    stmt.setString(3, Bukkit.getServerName());
                                }
                                stmt.executeUpdate();
                            } catch (SQLException ex) {
                                NetworkUtil.handleError(ex);
                            }
                        }
                    }.runTaskLater(RPICore.getInstance(), 30L);

                    new Thread() {
                        public void run() {
                            try {
                                stats = new Stats(0, 0);

                                PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `stats` WHERE `uuid`=?");
                                {
                                    stmt.setString(1, getUUID().toString());
                                }
                                ResultSet res = stmt.executeQuery();
                                if (res.next()) {
                                    stats.setDeaths(res.getInt("deaths"));
                                    stats.setKills(res.getInt("kills"));
                                } else {
                                    PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `stats` VALUES (?,?,?)");

                                    insert.setString(1, getUUID().toString());
                                    insert.setInt(2, 0);
                                    insert.setInt(3, 0);

                                    insert.executeUpdate();
                                }
                            } catch (SQLException ex) {
                                NetworkUtil.handleError(ex);
                            }
                        }
                    }.start();

                    new Thread() {
                        public void run() {
                            try {
                                toggles = new PlayerToggles(true, false, true);

                                PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `toggles` WHERE `rpid`=?");
                                {
                                    stmt.setInt(1, getRPID());
                                }
                                ResultSet res = stmt.executeQuery();
                                if(res.next()) {
                                    toggles.setIncognito(res.getBoolean("incognito"));
                                    toggles.setPlayerMessages(res.getBoolean("playerMessages"));
                                    toggles.setChatAcknowledgements(res.getBoolean("chatAcknowledgements"));
                                } else {
                                    PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `toggles` VALUES (?,?,?,?)");
                                    {
                                        insert.setInt(1, getRPID());
                                        insert.setBoolean(2, true);
                                        insert.setBoolean(3, false);
                                        insert.setBoolean(4, true);
                                    }
                                }
                            } catch (SQLException ex) {
                                NetworkUtil.handleError(ex);
                            }
                        }
                    }.start();
                }

            } catch (SQLException ex) {
                NetworkUtil.handleError(ex);
            }
        } else {
            Bukkit.getPlayer(name).kickPlayer(ChatColor.RED + "Could not load/create your data :(");
        }
    }

    public synchronized void saveData() {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `accounts` SET `username`=?,`rank`=?,`credits`=?,`ip`=? WHERE `uuid`=?");
                    {
                        stmt.setString(1, getPlayer().getName());
                        stmt.setString(2, getRank().toString());
                        stmt.setInt(3, getCredits());
                        stmt.setString(4, getIP().replace(".", "-"));
                        stmt.setString(5, getUUID().toString());
                    }

                    stmt.executeUpdate();

                    saveStats();
                    saveToggles();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public synchronized void saveStats() {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `stats` SET `kills`=?,`deaths`=? WHERE `uuid`=?");
                    {
                        stmt.setInt(1, getStats().getKills());
                        stmt.setInt(2, getStats().getDeaths());
                        stmt.setString(3, getUUID().toString());
                    }
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public synchronized void saveToggles() {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `toggles` SET `chatAcknowledgements`=?,`incognito`=?,`playerMessages`=? WHERE `rpid`=?");
                    {
                        stmt.setBoolean(1, toggles.isChatAcknowledgements());
                        stmt.setBoolean(2, toggles.isIncognito());
                        stmt.setBoolean(3, toggles.isPlayerMessages());
                        stmt.setInt(4, getRPID());
                    }
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
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

            setIP(ipAddress, true);
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
                return getRank().getFormattedName() + getTeam().getColor() + name;
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
            return getRank().getFormattedName() + getRank().getColor() + name;
        } else {
            return getRank().getColor() + name;
        }
    }

    public String getColoredName(boolean hasTag) {
        if (hasTag) {
            if(hasNameColor()) {
                return getRank().getFormattedName() + getNamecolor() + name;
            } else {
                return getRank().getFormattedName() + getRank().getColor() + name;
            }
        } else {
            if(hasNameColor()) {
                return getNamecolor() + name;
            } else {
                return getRank().getColor() + name;
            }
        }
    }

    public void updateNametag() {
        NametagAPI.setNametagHard(name,
                String.valueOf(rank == Rank.PLAYER ? (getTeam() != null ? getTeam().getColor() : ChatColor.WHITE) : getRank().getFormattedName() + (getTeam() != null ? getTeam().getColor() : ChatColor.WHITE)),
                (toggles.isInvisForIncognito() ? ChatColor.BLUE.toString() + ChatColor.BOLD + " [INCOGNITO]" : ""));
    }

    private RPlayer getThis() {
        return this;
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

    public void setIP(String ip, boolean save) {
        this.ip = ip;
        if(save) {
            saveData();
        }
    }

    public String getIP() {
        return ip;
    }

    public void setRank(final Rank rank, boolean saveData) {
        if (rank != getRank()) {

            this.rank = rank;
            if (saveData) {
                saveData();
            }

            if (has(Rank.MOD)) {
                staff = new RStaff(this);
            }
        }

        for (RPlayer cur : PlayerManager.getInstance().getOnlinePlayers()) {
            if (cur.getToggles().isInvisForIncognito()) {
                if (!has(Rank.VIP)) {
                    getPlayer().hidePlayer(cur.getPlayer());
                }
            }
        }

        updateNametag();
        ScoreboardManager.getInstance().updateScoreboard(this);
    }

    public void setDataRank(final Rank rank, boolean saveData) {
        if (rank != getRank()) {

            this.rank = rank;
            if (saveData) {
                saveData();
            }

            if (has(Rank.MOD)) {
                staff = new RStaff(this);
            }
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

    public boolean isBanned() {
        return PunishmentManager.getInstance().hasBanActive(getUUID());
    }

    public boolean isMuted() {
        return PunishmentManager.getInstance().hasMuteActive(getUUID());
    }

    public Punishment getMute() {
        return PunishmentManager.getInstance().getActiveMute(getUUID());
    }

    public void addCredits(int credits, boolean bonus) {
        String reason = null;
        int credAdded = credits;
        if(bonus) {
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
                case DEV:
                    credAdded = credits * 7;
                    reason = " §3§lx7 Developer Bonus";
                    break;
                case ADMIN:
                    credAdded = credits * 8;
                    reason = " §c§lx8 Admin Bonus";
                    break;
                case OWNER:
                    credAdded = credits * 10;
                    reason = " §4§lx10 Owner Bonus";
                    break;
            }

            /*
            if (getBooster() != null) {
                if (getBooster().isActive()) {
                    credAdded = credAdded * getBooster().getMultiplier();
                    reason = reason + " §1§lx" + getBooster().getMultiplier() + " Credit Booster";
                } else {
                    getBooster().deactivate();
                    message(MsgType.INFO, "Your 1 day credit booster has now ended.");
                }
            }
            */
        }

        this.credits = getCredits() + credAdded;
        saveData();

        if (reason != null) {
            message(MsgType.CREDIT, "§a+" + credAdded + " Credits" + reason);
        } else {
            message(MsgType.CREDIT, "§a+" + credAdded + " Credits");
        }

        ScoreboardManager.getInstance().updateScoreboard(this);
    }

    public void removeCredits(int credits) {
        this.credits = getCredits() - credits;
        message(MsgType.CREDIT, "§c-" + credits + " Credits");
        ScoreboardManager.getInstance().updateScoreboard(this);
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCredits() {
        //This is so if their credits are set on another server that it doesn't override if they're current is lower
        int databaseCreds;
        {
            Future<Integer> task = Executors.newCachedThreadPool().submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `credits` FROM `accounts` WHERE `rpid`=?");
                    {
                        stmt.setInt(1, getRPID());
                    }
                    return stmt.executeQuery().getInt("credits");
                }
            });
            try{ databaseCreds = task.get(); }catch(Exception ex){ databaseCreds = 0; }
        }
        if(databaseCreds-credits >= 1)  {
            credits = databaseCreds;
            return databaseCreds;
        } else {
            return credits;
        }
    }

    public void setBooster(Booster boost) {
        this.booster = boost;
    }

    public Booster getBooster() {
        return booster;
    }

    public boolean hasNameColor() {
        return namecolor != null;
    }

    public ChatColor getNamecolor() {
        return namecolor;
    }

    public void setNamecolor(ChatColor namecolor) {
        this.namecolor = namecolor;
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

    public Stats getStats() {
        return stats;
    }

    public PlayerToggles getToggles() {
        return toggles;
    }

    public Team getLastTeam() {
        return lastTeam;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean joinTeam(Team t) {
        if(team != null) this.lastTeam = team; else this.lastTeam = t;
        this.team = t;

        return TeamManager.joinTeam(this, t);
    }

    public void leaveTeam(Team team) {
        team.removePlayer(this);
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

    public double getHealth() {
        return getPlayer().getHealth();
    }

    public void setHealth(double h) {
        getPlayer().setHealth(h);
    }

    public void kill(EntityDamageEvent cause) {
        getPlayer().setLastDamageCause(cause);
        setHealth(0);
    }

    public void kill(EntityDamageEvent.DamageCause cause) {
        kill(new EntityDamageEvent(getPlayer(), cause, getHealth()));
    }

    public void kill() {
        kill(EntityDamageEvent.DamageCause.VOID);
    }

    public void damage(double d) {
        setHealth(getHealth() - d);
    }

    public void heal(double h) {
        setHealth(getHealth() + h);
    }

    public String getFormattedHealth() {
        return "" + NumberUtil.trimNumber((double) getPlayer().getHealth());
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public Inventory getInventory() {
        return getPlayer().getInventory();
    }

    public void closeInventory() {
        getPlayer().closeInventory();
    }

    public void openInventory(Inventory inv) {
        getPlayer().openInventory(inv);
    }

    public void openInventory(RInventory inv) {
        openInventory(inv.getInventory());
    }

    public void playSound(Location loc, Sound sound, float f1, float f2) {
        getPlayer().playSound(loc, sound, f1, f2);
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
        setCompassTarget(player.getLocation());
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

    public void send(String server) {
        message(MsgType.NETWORK, "§aConnecting you to §b" + server.toUpperCase() + "§a...");

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server.toUpperCase());
            getPlayer().sendPluginMessage(RPICore.getInstance(), "BungeeCord", b.toByteArray());
        } catch (IOException ex) {
            NetworkUtil.handleError(ex);
            message(ChatColor.RED + "Cannot connect you to the specified server at this time");
        }
    }

}
