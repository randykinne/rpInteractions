package RandomPvP.Core.Chat;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.UUIDCache;
import RandomPvP.Core.Punishment.PunishmentManager;
import RandomPvP.Core.Util.RPStaff;
import RandomPvP.Core.Util.ServerToggles;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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
public class ChatHandler implements Listener {
    private PunishmentManager manager;

    public ChatHandler(PunishmentManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {


        RPlayer pl = RPlayerManager.getInstance().getPlayer(e.getPlayer());

        PunishmentManager.Punishment punishment;
        if ((punishment = manager.hasActivePunishment(pl.getUUID(), PunishmentManager.PunishmentType.MUTE)) != null) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§4§l>> §7Shh! You are muted for " + punishment.getReason() + ".");
        } else {
            String message = e.getMessage();
            String standardFormat = pl.getDisplayName(true) + "§8: §f" + message;
            String format;
            if (pl.getTeam() != null) {
                if (pl.getTeam().isShownInChat()) {
                    format = "§8[" + pl.getTeam().getColor() + pl.getTeam().getName() + "§8] " + pl.getDisplayName(true) + "§8: §f" + message;
                } else {
                    format = standardFormat;
                }
            } else {
                format = standardFormat;
            }

            e.setFormat(String.format(format));

            if (message.startsWith("!") && pl.isStaff()) {
                e.setCancelled(true);

                if (!pl.hasSTFUEnabled()) {
                    RPStaff.sendStaffMessage(pl.getRankedName(true) + "§8: §6" + message.replace("!", ""), true);
                }
            }

            if (!ServerToggles.isChatEnabled() && !pl.isVIP()) {
                e.setCancelled(true);
                pl.message("§4§l>> §7Shh! You may not speak when chat is disabled!");
            }
        }

        if (!e.isCancelled()) {
            String msg = e.getMessage();
            Player sender = e.getPlayer();
            String query = "INSERT INTO chatlog (uuid,message) VALUES ('" + UUIDCache.getUUID(sender.getName()) + "','" + msg + "')";
            ChatLogger.logChat(query);
        }
    }
}
