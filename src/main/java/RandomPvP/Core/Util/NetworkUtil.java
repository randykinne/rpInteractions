package RandomPvP.Core.Util;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.RPICore;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class NetworkUtil {

    public static String[] getOnlineStaff() {
        return new OnlineStaff().online;
    }

    private static class OnlineStaff {
        String[] online;
        public OnlineStaff() {
            new BukkitRunnable() {
                public void run() {
                    try {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `online_players`;");
                        ResultSet set = stmt.executeQuery();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(RPICore.getInstance());
        }
    }
}
