package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Util.ServerToggles;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
public class SilenceChatCmd {

    @Command(aliases = "silencechat", desc = "Disables chat for non staff members", usage = "Toggles chat for non staff members")
    public static void chat(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.isStaff()) {
                if (ServerToggles.isChatEnabled()) {
                    ServerToggles.setChatEnabled(false);
                    /*Bukkit.broadcastMessage("§c╔══════════════════════════════════");
                    Bukkit.broadcastMessage("§c║ §4§lCHAT SILENCED! §7Requested by " + pl.getRankedName(false));
                    Bukkit.broadcastMessage("§c║ §7All non-staff members have been silenced.");
                    Bukkit.broadcastMessage("§c╚══════════════════════════════════");
                    */
                    Bukkit.broadcastMessage("§4§m----------------------------------------------------");
                    Bukkit.broadcastMessage("\n");
                    Bukkit.broadcastMessage("§c§lCHAT SILENCED! §7Requested by " + pl.getRankedName(false));
                    Bukkit.broadcastMessage("\n");
                    Bukkit.broadcastMessage("§7All non-staff members have been silenced.");
                    Bukkit.broadcastMessage("\n");
                    Bukkit.broadcastMessage("§4§m----------------------------------------------------");
                } else {
                    ServerToggles.setChatEnabled(true);
                    /*
                    Bukkit.broadcastMessage("§a╔══════════════════════════════════");
                    Bukkit.broadcastMessage("§a║ §2§lYOU MAY SPEAK AGAIN!! §7Requested by " + pl.getRankedName(false));
                    Bukkit.broadcastMessage("§a║ §7Chat has been returned to normal state, all players may now speak.");
                    Bukkit.broadcastMessage("§a╚══════════════════════════════════");
                    */

                    Bukkit.broadcastMessage("§2§m----------------------------------------------------");
                    Bukkit.broadcastMessage("\n");
                    Bukkit.broadcastMessage("§a§lYOU MAY SPEAK AGAIN! §7Requested by " + pl.getRankedName(false));
                    Bukkit.broadcastMessage("\n");
                    Bukkit.broadcastMessage("§7Chat has been returned to normal state.");
                    Bukkit.broadcastMessage("\n");
                    Bukkit.broadcastMessage("§2§m----------------------------------------------------");
                }
            } else {
                throw new CommandException("§7You need to be §oMod §7to use this command.");
            }
        }
    }
}
