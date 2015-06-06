package RandomPvP.Core.Player;

import RandomPvP.Core.Data.Json.JsonDataSet;
import RandomPvP.Core.Data.Json.RDatabaseWebCall;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.Stats.Stats;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.Player.UUID.UUIDFetcher;
import RandomPvP.Core.Util.Player.UUID.UUIDUtil;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class OfflineRPlayer {

    private boolean isnull = true;
    private String name = "Name Error";
    private UUID uuid = null;
    private String ip = null;
    private Rank rank = Rank.PLAYER;
    private int credits = 0;
    private int rpid = -1;
    private Stats stats = null;


    public OfflineRPlayer(final String name) {
        uuid = UUIDUtil.getUUID(name);

        this.name = name;

        if (uuid == null) {
            isnull = true;
        } else {
            /*
            {
                JsonDataSet set = new RDatabaseWebCall("accounts").call();

                if (set.getWhere("uuid", uuid.toString()) != null) {
                    JSONObject obj = set.getWhere("uuid", uuid.toString());

                    setRank(Rank.valueOf((String) obj.get("rank")));
                    setCredits(Integer.valueOf((String) obj.get("credits")));
                    ip = ((String) obj.get("ip")).replace("-", ".");
                    setRPID(Integer.valueOf((String) obj.get("rpid")));

                    isnull = false;
                }
            }

            if(!isnull) {
                {
                    stats = new Stats(0, 0);

                    JsonDataSet set = new RDatabaseWebCall("stats").call();

                    if (set.getWhere("uuid", uuid.toString()) != null) {
                        JSONObject obj = set.getWhere("uuid", uuid.toString());

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
            }
            */
            new Thread() {
                public void run() {
                    try {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `uuid` = ?;");
                        stmt.setString(1, OfflineRPlayer.this.getUUID().toString());
                        ResultSet set = stmt.executeQuery();

                        if (set.next()) {
                            OfflineRPlayer.this.setRank(Rank.valueOf(set.getString("rank").toUpperCase()));
                            OfflineRPlayer.this.setCredits(set.getInt("credits"));
                            OfflineRPlayer.this.ip = set.getString("ip").replace("-", ".");
                            OfflineRPlayer.this.setRPID(set.getInt("rpid"));
                            OfflineRPlayer.this.isnull = false;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
                    .start();

            new Thread() {
                public void run() {
                    try {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `stats` WHERE `uuid`=?");

                        stmt.setString(1, OfflineRPlayer.this.getUUID().toString());

                        ResultSet res = stmt.executeQuery();

                        int kills = 0;
                        int deaths = 0;

                        if (res.next()) {
                            kills = res.getInt("kills");
                            deaths = res.getInt("deaths");
                        } else {
                            PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `stats` VALUES (?,?,?)");

                            insert.setString(1, OfflineRPlayer.this.getUUID().toString());
                            insert.setInt(2, 0);
                            insert.setInt(3, 0);

                            insert.executeUpdate();
                        }

                        OfflineRPlayer.this.stats = new Stats(kills, deaths);
                    } catch (SQLException ex) {
                        NetworkUtil.handleError(ex);
                    }
                }
            }.start();
        }
    }

    public OfflineRPlayer(final int rpid) {
        this.rpid = rpid;

        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `accounts` WHERE `rpid` = ?;");
                    stmt.setInt(1, getRPID());
                    ResultSet set = stmt.executeQuery();

                    if (set.next()) {
                        setRank(Rank.valueOf(set.getString("rank").toUpperCase()));
                        setCredits(set.getInt("credits"));
                        ip = set.getString("ip").replace("-", ".");
                        setName(set.getString("username"));

                        try {
                            uuid = new UUIDFetcher(Arrays.asList(name), false).call().get(name);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        if(uuid != null) {
            isnull = false;

            new Thread() {
                public void run(){
                    try {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `stats` WHERE `uuid`=?");
                        {
                            stmt.setString(1, getUUID().toString());
                        }
                        ResultSet res = stmt.executeQuery();

                        int kills = 0;
                        int deaths = 0;

                        {
                            if (res.next()) {
                                kills = res.getInt("kills");
                                deaths = res.getInt("deaths");
                            } else {
                                PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `stats` VALUES (?,?,?)");
                                {
                                    insert.setString(1, getUUID().toString());
                                    insert.setInt(2, 0);
                                    insert.setInt(3, 0);
                                }

                                insert.executeUpdate();
                            }
                        }

                        stats = new Stats(kills, deaths);
                    } catch (SQLException ex) {
                        NetworkUtil.handleError(ex);
                    }
                }
            }.start();
        } else {
            stats = new Stats(0, 0);
            isnull = true;
        }
    }

    public OfflineRPlayer(UUID uuid) {
         this(Bukkit.getOfflinePlayer(uuid).getName());
    }

    public synchronized void saveData() {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `accounts` SET `username`=?,`rank`=?,`credits`=?,`ip`=? WHERE `uuid`=?");
                    {
                        stmt.setString(1, getPName());
                        stmt.setString(2, getRank().toString());
                        stmt.setInt(3, getCredits());
                        stmt.setString(4, getIP().replace(".", "-"));
                        stmt.setString(5, getUUID().toString());
                    }

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public boolean isNull() {
        return isnull;
    }
    public String getName() {
        return name;
    }
    public String getPName() { return name; }
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

    public Stats getStats() {
        return stats;
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
