package RandomPvP.Core.Server.Punish;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class PunishmentManager extends PunishmentDatabaseConnection {
    public PunishmentManager(DatabaseConnectionFactory factory) {
        super(factory);
    }

    private static PunishmentManager instance = new PunishmentManager(
            DatabaseConnectionFactory.builder()
                    .withHost(MySQL.getAddress())
                    .withPort(MySQL.getPort())
                    .withDatabase(MySQL.getDatabase())
                    .withUsername(MySQL.getUsername())
                    .withPassword(MySQL.getPassword())
    );

    public static PunishmentManager getInstance() {
        return instance;
    }

    /**
     * Check the punishments database
     */
    public void checkDatabase() {
        try {
            this.getPreparedStatement("CREATE TABLE IF NOT EXISTS punishments (id INTEGER, punished VARCHAR(255), issuer VARCHAR(255), type VARCHAR(255), reason TEXT, time VARCHAR(255), end LONG, active BOOL)").execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String generateMessage(Punishment pm) {

       String message = "";

        if (pm.getType() == PunishmentType.PERMANENT_BAN || pm.getType() == PunishmentType.TEMPORARY_BAN) {
            message = "\n    §4§lBANNED! §e" + NumberUtil.translateDuration(pm.getEnd()) + "\n" +
                    "  §fReason? §e§n" + pm.getReason() + "\n" +
                    "§f    Staff? " + new OfflineRPlayer(Bukkit.getOfflinePlayer(pm.getIssuer()).getName()).getRankedName(false) + "\n" +
                    " §cAppeal at http://randompvp.com/forum";
        } else if (pm.getType() == PunishmentType.WARN) {
            message = "§4§l>> §6§lWARNING§8: §7You have recieved one warning for §3§n" + pm.getReason() + "§7. \n§4§l>> §6If you continue, you may be kicked or banned.";
        } else if(pm.getType() == PunishmentType.KICK) {
            message = "\n    §c§lKICKED!\n" +
                    "  §fReason? §e§n" + pm.getReason() + "\n" +
                    "§f    Staff? " + new OfflineRPlayer(Bukkit.getOfflinePlayer(pm.getIssuer()).getName()).getRankedName(false) + "\n";
        } else if(pm.getType() == PunishmentType.PERMANENT_MUTE || pm.getType() == PunishmentType.TEMPORARY_MUTE) {
            message = "§4§l>> §7You have been §6§lMUTED§7 (§6" + NumberUtil.translateDuration(pm.getEnd()) + ChatColor.GRAY + ")! Reason? §3§n" + pm.getReason() + "§7. \n§4§l>> §cAppeal at http://randompvp.com/forums";
        }

        return message;
    }


    /**
     * Get a new id for a punishment
     *
     * @return New id for a new punishment
     */
    public int getNewId() {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT COUNT(*) FROM punishments").executeQuery();
            rs.next();

            return rs.getInt(1) + 1;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Get a punishment by it's id
     *
     * @param id ID of punishment to get
     * @return Punish by it's id
     */
    public Punishment getPunishment(int id) {
        try {
            ResultSet rs = this.getPreparedStatement("SELECT * FROM punishments WHERE id=" + id + ";").executeQuery();

            if (rs.next()) {
                Punishment punishment = new Punishment(
                        UUID.fromString(rs.getString("punished")),
                        UUID.fromString(rs.getString("issuer")),
                        PunishmentType.fromString(rs.getString("type")),
                        rs.getString("reason"),
                        rs.getString("time"),
                        rs.getLong("end")
                );
                punishment.setId(rs.getInt("id"));
                punishment.setActive(rs.getBoolean("active"));
                return punishment;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            NetworkUtil.handleError(ex);
        }
        return null;
    }

    /**
     * Get the punishments for a player
     *
     * @param uuid UUID of player to get punishments for
     * @return Player's punishments
     */
    public Punishment[] getPunishments(UUID uuid) {
        try {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `punishments` WHERE `punished`=?");
            {
                stmt.setString(1, uuid.toString());
            }
            ResultSet rs = stmt.executeQuery();
            List<Punishment> punishments = new ArrayList<>();
            while (rs.next()) {
                Punishment punishment = new Punishment(
                        UUID.fromString(rs.getString("punished")),
                        UUID.fromString(rs.getString("issuer")),
                        PunishmentType.fromString(rs.getString("type")),
                        rs.getString("reason"),
                        rs.getString("time"),
                        rs.getLong("end")
                );
                punishment.setId(rs.getInt("id"));
                punishment.setActive(rs.getBoolean("active"));
                punishments.add(punishment);
            }

            return punishments.toArray(new Punishment[punishments.size()]);
        } catch (SQLException ex) {
            NetworkUtil.handleError(ex);
        }
        return new Punishment[0];
    }

    /**
     * Check if a player has an active ban
     *
     * @param player UUID of player
     * @return true if player has a ban active
     */
    public boolean hasBanActive(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_BAN || punishment.getType() == PunishmentType.TEMPORARY_BAN) && punishment.isActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a player's active ban
     *
     * @param player UUID of player
     * @return Player's active ban if they have one
     */
    public Punishment getActiveBan(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_BAN || punishment.getType() == PunishmentType.TEMPORARY_BAN) && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    /**
     * Check if a player has an active mute
     *
     * @param player UUID of player
     * @return true if player has a mute active
     */
    public boolean hasMuteActive(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && punishment.isActive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a player's active ban
     *
     * @param player UUID of player
     * @return Player's active ban if they have one
     */
    public Punishment getActiveMute(UUID player) {
        for (Punishment punishment : getPunishments(player)) {
            if ((punishment.getType() == PunishmentType.PERMANENT_MUTE || punishment.getType() == PunishmentType.TEMPORARY_MUTE) && punishment.isActive()) {
                return punishment;
            }
        }
        return null;
    }

    /**
     * Get the latest punishment of a player
     *
     * @param player UUID of player to get punishment for
     * @return Latest punishment of player
     */
    public Punishment getLatestPunishment(UUID player) {
        Punishment[] punishments = getPunishments(player);
        return punishments[punishments.length - 1];
    }

}