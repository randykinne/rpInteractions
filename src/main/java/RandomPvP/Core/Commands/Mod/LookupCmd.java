package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Player.UUIDCache;
import RandomPvP.Core.Punishment.PunishmentManager.Punishment;
import RandomPvP.Core.Punishment.PunishmentManager;
import RandomPvP.Core.Punishment.Util;
import RandomPvP.Core.Util.NumberUtil;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class LookupCmd {
    private static PunishmentManager manager;

    public LookupCmd(PunishmentManager manager) {
        this.manager = manager;
    }

    static RPlayer player = null;
    static String displayname = "hi";
    static String name = "hi";
    static UUID id;
    static int credits = 0;
    static Rank rank = null;
    static String IP = null;
    static int rpid;

    @Command(aliases = {"lookup", "info"}, desc = "Returns info about a player", max = 1, min = 1)
    public static void lookup(final CommandContext args, CommandSender sender) throws CommandException {
        boolean allowed = false;
        boolean isnull = true;
        RPlayer pl;


        boolean online = false;
        if (sender instanceof Player) {
            pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.isStaff()) {
                allowed = true;
            }
        } else {
            allowed = true;
        }

        if (allowed) {
            if (Bukkit.getPlayer(args.getString(0)) != null) {
                player = RPlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args.getString(0)));
                displayname = player.getRankedName(false);
                name = player.getName();
                id = player.getUUID();
                credits = player.getCredits();
                rank = player.getRank();
                IP = player.getIP();
                rpid = player.getRPID();
                online = true;
                isnull = false;
            } else {
                OfflineRPlayer playa = new OfflineRPlayer(args.getString(0), UUIDCache.getUUID(args.getString(0)));
                isnull = playa.isNull();
                displayname = playa.getRankedName(false);
                name = playa.getName();
                id = playa.getUUID();
                credits = playa.getCredits();
                rank = playa.getRank();
                IP = playa.getIP();
                rpid = playa.getRPID();
            }

            if (!isnull) {
                sender.sendMessage("§4§l>> §4Account Information for " + displayname + "§4:");
                sender.sendMessage("§f - §7Mojang ID: §8" + id);
                sender.sendMessage("§f - §7RandomPvP ID: §8" + rpid);
                sender.sendMessage("§f - §7Rank: " + rank.getName());
                sender.sendMessage("§f - §7Credits: §8" + credits);
                sender.sendMessage("§f - §7IP: §8" + IP);
                if (online) {
                    sender.sendMessage("§f - §7Health: §8" + player.getPlayer().getHealthScale());
                    //sender.sendMessage("§f - §7Location: §8" + String.valueOf(NumberUtil.trimNumber(player.getPlayer().getLocation().getX())) + ", " + String.valueOf(NumberUtil.trimNumber(player.getPlayer().getLocation().getY())) + ", " + String.valueOf(NumberUtil.trimNumber(player.getPlayer().getLocation().getZ())));
                    sender.sendMessage("§f - §7Gamemode: §8" + player.getPlayer().getGameMode().toString());
                }


                if (manager.getAllPunishmentsFor(id).size() > 0) {
                    sender.sendMessage("§4§lPunishments: §6(" + manager.getAllPunishmentsFor(id).size() + ")");
                    for (Punishment punishment : manager.getAllPunishmentsFor(id)) {
                        String modifier = "";
                        String message = "§a#" + modifier + punishment.getId() + " " + ChatColor.GRAY + modifier + Util.formatTimestamp(punishment.getCreated()) + " §8> " + ChatColor.WHITE + modifier + punishment.getType().getName() + " by " + ChatColor.GRAY + modifier + (punishment.getAdmin() == null ? "§4AutoModerator" : new OfflineRPlayer(UUIDCache.getName(punishment.getAdmin()), punishment.getAdmin()).getRankedName(false) + modifier +"§8: §7§n" + punishment.getReason());
                        if (punishment.hasExpired()) {
                            modifier = "§m";
                        }
                        sender.sendMessage(message);



                    }
                } else {
                    sender.sendMessage("§a§lNo punishments on record.");
                }
            } else {
                sender.sendMessage("§4§l>> §7Player not found in database.");
            }
        } else {
            sender.sendMessage("§4§l>> §7You need to be Mod to use this command.");
        }
    }
}
