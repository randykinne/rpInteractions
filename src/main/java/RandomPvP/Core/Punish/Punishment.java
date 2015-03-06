package RandomPvP.Core.Punish;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Util.Player.UUID.UUIDUtil;
import org.bukkit.Bukkit;

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
public class Punishment {
    private UUID punished, issuer;
    private PunishmentType type;
    private String reason;
    private long end;
    private int id = PunishmentManager.getInstance().getNewId();
    private String time;
    private boolean active = true;

    public Punishment(UUID punished, UUID issuer, PunishmentType type, String reason, String time, long end) {
        this.punished = punished;
        this.issuer = issuer;
        this.type = type;
        this.reason = reason;
        this.time = time;
        this.end = end;
    }

    /**
     * Get the punished uuid
     *
     * @return Punished UUID
     */
    public UUID getPunished() {
        return this.punished;
    }

    public String getPunishedIP() {
        return new OfflineRPlayer(UUIDUtil.getName(getPunished())).getIP();
    }

    /**
     * Get the issuer of the punishment's UUID
     *
     * @return Issuer of punishment's UUID
     */
    public UUID getIssuer() {
        return this.issuer;
    }

    /**
     * Get the type of the punishment
     *
     * @return Type of the punishment
     */
    public PunishmentType getType() {
        return this.type;
    }

    /**
     * Get the reasoning for the punishment
     *
     * @return Reason for the punishment being issued
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Get the ending of the punishment
     *
     * @return Ending of the punishment
     */
    public long getEnd() {
        return this.end;
    }

    /**
     * Get the id of the punishment
     *
     * @return ID of the punishment
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set the id of the punishment
     *
     * @param id ID of punishment
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the time the punishment was created
     *
     * @return time
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Check if the punishment is active
     *
     * @return true if the punishment is active
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Set the punishment active flag
     *
     * @param flag Punish active flag
     */
    public void setActive(boolean flag) {
        this.active = flag;
    }

    /**
     * Save the punishment to the database
     */
    public void save() {
        try {
            PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT COUNT(*) FROM punishments WHERE id=?;");
            statement.setInt(1, getId());
            ResultSet res = statement.executeQuery();
            {
                res.next();
            }
            if (res.getInt(1) == 0) {
                PreparedStatement stmt = MySQL.getConnection().prepareStatement("INSERT INTO `punishments` (id, punished, issuer, type, reason, time, end, active) VALUES (?,?,?,?,?,?,?,?)");
                stmt.setInt(1, getId());
                stmt.setString(2, getPunished().toString());
                stmt.setString(3, getIssuer().toString());
                stmt.setString(4, getType().getName());
                stmt.setString(5, getReason());
                stmt.setString(6, getTime());
                stmt.setLong(7, getEnd());
                stmt.setBoolean(8, isActive());

                stmt.executeUpdate();
            } else {
                PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `punishments` SET punished=?, issuer=?, type=?, reason=?, time=?, end=?, active=? WHERE id=?");
                stmt.setString(1, getPunished().toString());
                stmt.setString(2, getIssuer().toString());
                stmt.setString(3, getType().getName());
                stmt.setString(4, getReason());
                stmt.setString(5, getTime());
                stmt.setLong(6, getEnd());
                stmt.setBoolean(7, isActive());
                stmt.setInt(8, getId());

                stmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
