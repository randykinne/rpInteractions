package RandomPvP.Core.Chat;

import RandomPvP.Core.Event.Player.RPlayerChatEvent;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentType;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Broadcasts;
import RandomPvP.Core.Util.NumberUtil;
import RandomPvP.Core.Util.ServerToggles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
        if (pl != null) {
            if (RPICore.pEnabled) {
                if (pl.isMuted()) {
                    Punishment pm = pl.getMute();
                    boolean allowed = true;
                    if (pm.getType() == PunishmentType.TEMPORARY_MUTE) {
                        if (pm.getEnd() - System.currentTimeMillis() >= 0L) {
                            pm.setActive(false);
                            pm.save();
                        } else {
                            e.setCancelled(true);
                            pl.message("§4§l>> §eYou are §c§lMUTED§e. Reason? §f§n" + pm.getReason() + "§e. Expires? " + NumberUtil.translateDuration(pm.getEnd()));
                        }
                    } else {
                        e.setCancelled(true);
                        pl.message("§4§l>> §eYou are §c§lMUTED§e. Reason? §f§n" + pm.getReason() + "§e. Expires? " + NumberUtil.translateDuration(pm.getEnd()));
                    }
                }
            }


                String message = e.getMessage().replace("%", "percent");
                String standardFormat = pl.getDisplayName(true) + " §f" + message;
                String format;
                if (pl.getTeam() != null) {
                    if (pl.getTeam().isShownInChat()) {
                        format = pl.getTeam().getColor() + pl.getTeam().getName() + " " + pl.getDisplayName(true) + " §f" + message;
                    } else {
                        format = standardFormat;
                    }
                } else {
                    format = standardFormat;
                }

                e.setFormat(String.format(format));

                if (pl.has(Rank.MOD)) {
                    if (e.getMessage().startsWith("!") || pl.getStaff().hasStaffChatToggled()) {
                        e.setCancelled(true);

                        if (!pl.getStaff().hasSTFUEnabled()) {
                            if (e.getMessage().startsWith("!")) {
                                StringBuilder string = new StringBuilder(message);
                                string.deleteCharAt(0);
                                if(!(string.toString().equals(""))) {
                                    Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(true) + "§8: §3" + string.toString());
                                } else {
                                    pl.message(MsgType.ERROR, "You must supply a message!");
                                }
                            } else {
                                Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(true) + "§8: §3" + e.getMessage());
                            }
                        }
                    }
                }

                if (!ServerToggles.isChatEnabled() && !pl.has(Rank.VIP)) {
                    e.setCancelled(true);
                    pl.message("§4§l>> §7Shh! You may not speak when chat is disabled!");
                }

                if (!e.isCancelled()) {

                    Bukkit.getPluginManager().callEvent(new RPlayerChatEvent(pl, e.getFormat()));
                /*
                String msg = e.getMessage();
                Player sender = e.getPlayer();
                String query = "INSERT INTO chatlog (uuid,message) VALUES ('" + UUIDCache.getUUID(sender.getName()) + "','" + msg + "')";
                ChatLogger.logChat(query);
                */
                }
        } else {
            e.getPlayer().sendMessage(ChatColor.RED + "Your data was equal to null... what?");
            e.setCancelled(true);
        }
    }
}
