package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.RPICore;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

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
public class RPICoreInfoCommand {
    @Command(aliases = { "game", "ver", "version", "rpi", "rpicore", "core", "?", "pl" },
    desc = "Returns info about RPI", usage = "- ", max = 1, min = 0)
    public static void command(final CommandContext args, CommandSender sender) throws CommandException {
        PluginDescriptionFile pdf = new RPICoreInfoCommand().getDescription();
        sender.sendMessage("§8§l>> §cRunning §4§l" + pdf.getName() + " §cbuild " + pdf.getVersion());
        sender.sendMessage("§8§l>> §cDeveloped by §4Randomizer27§c.");
    }

    public PluginDescriptionFile getDescription() {
        return RPICore.getInstance().getDescription();
    }
}
