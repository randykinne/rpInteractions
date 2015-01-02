package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
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
public class SayCmd {

    @Command(aliases = "say", usage = "[message]", desc = "Broadcasts something to the whole server", min = 1)
    public static void say(final CommandContext args, CommandSender sender) throws CommandException {
        String username = "";
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.isStaff()) {
                username = pl.getRankedName(false);
                Bukkit.broadcastMessage("§8[§4§l§oSAY§8] " + username + "§8: §b" + args.getJoinedStrings(0));
            } else {
                throw new CommandException("§7You need to be §oMod §7to use this command.");
            }
        } else {
            username = "§d§lConsole";
        }
    }
}
