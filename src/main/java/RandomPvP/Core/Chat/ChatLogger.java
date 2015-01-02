package RandomPvP.Core.Chat;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.RPICore;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
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
public class ChatLogger {

    static List<String> chat = new ArrayList<>();

    public static synchronized  void logChat(String sql) {
        /* TODO FIX + IMPLEMENT
        chat.add(sql);
        scheduleLog();
        */
    }

    private static void scheduleLog() {
        new BukkitRunnable() {
            @Override
        public void run() {
                for (String sql : chat) {
                    try {
                        PreparedStatement statement = MySQL.getConnection().prepareStatement(sql);
                        statement.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (chat.size() == 0) {
                        cancel();
                    }
                }
            }
        }.runTaskTimerAsynchronously(RPICore.getInstance(), 3600L, 3600L);
    }
}
