package RandomPvP.Core.Chat;

import RandomPvP.Core.Event.Player.RPlayerChatEvent;
import RandomPvP.Core.Listener.RPlayerJoinListener;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentType;
import RandomPvP.Core.Server.General.Messages;
import RandomPvP.Core.Util.NumberUtil;
import RandomPvP.Core.Server.General.ServerToggles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
            if (pl.isMuted()) {
                Punishment pm = pl.getMute();
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
                if(pl.hasNameColor()) {
                    format = pl.getColoredName(true) + " §f" + message;
                } else {
                    format = standardFormat;
                }
            }

            e.setFormat(format);

            if (pl.has(Rank.MOD)) {
                if (e.getMessage().startsWith("!") || pl.getStaff().hasStaffChatToggled()) {
                    e.setCancelled(true);

                    if (!pl.getStaff().hasSTFUEnabled()) {
                        if (e.getMessage().startsWith("!")) {
                            StringBuilder string = new StringBuilder(message);
                            string.deleteCharAt(0);
                            if(!(string.toString().equals(""))) {
                                Messages.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(true) + "§8: §3" + string.toString());
                            } else {
                                pl.message(MsgType.ERROR, "You must supply a message!");
                            }
                        } else {
                            Messages.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(true) + "§8: §3" + e.getMessage());
                        }
                    }
                }
            }

            if (!ServerToggles.isChatEnabled() && !pl.has(Rank.VIP)) {
                e.setCancelled(true);
                pl.message("§4§l>> §7Shh! You may not speak when chat is disabled!");
            }

            if (!e.isCancelled()) {
                RPlayerChatEvent ev = new RPlayerChatEvent(pl, e.getFormat());
                {
                    Bukkit.getPluginManager().callEvent(ev);
                }
                if(!ev.isCancelled()) {
                    if(pl.getToggles().isInvisForIncognito()) {
                        for (RPlayer cur : PlayerManager.getInstance().getOnlinePlayers()) {
                            if(!cur.getPlayer().canSee(pl.getPlayer())) {
                                cur.getPlayer().showPlayer(pl.getPlayer());
                            }
                        }

                        pl.message(MsgType.NETWORK, "Left " + ChatColor.BLUE.toString() + ChatColor.BOLD + "INCOGNITO MODE" + ChatColor.GRAY + ".");
                        pl.getToggles().setInvisForIncognito(false);

                        pl.updateNametag();
                    }

                    for(RPlayer cur : PlayerManager.getInstance().getOnlinePlayers()) {
                        if(message.contains("@" + cur.getName())) {
                            if(cur.getToggles().isChatAcknowledgements()) {
                                message.replace("@" + cur.getName(), ChatColor.YELLOW + "@" + cur.getName() + ChatColor.WHITE);
                                cur.playSound(cur.getLocation(), Sound.CAT_MEOW, 1F, 1F);
                            } else {
                                pl.message(MsgType.ERROR, cur.getDisplayName(false) + ChatColor.GRAY + " currently has chat acknowledgements turned off. They will not receive any notification about your message.");
                            }
                        }
                    }
                }
            }
        } else {
            e.getPlayer().sendMessage(ChatColor.RED + "Your data was equal to null... what?");
            e.setCancelled(true);
        }
    }
}
