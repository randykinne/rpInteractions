package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
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
public class WarningCmd {
    @Command(aliases = "warn", usage = "[player] [reason]", desc = "Warns a player with specified reason", min = 2)
    public static void warn(final CommandContext args, CommandSender sender) throws CommandException {
        RPlayer pl;
        if (sender instanceof Player) {
            pl = RPlayerManager.getInstance().getPlayer((Player) sender);
        } else {
            pl = null;
        }

        if (pl != null) {
            if (pl.isStaff()) {
                throw new CommandException("This command has been disabled.");
            }
        } else {
            throw new CommandException("§7You need to be §oMod §7to use this command.");
        }
    }
}