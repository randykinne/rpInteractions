package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.RPICore;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

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
public class HubCmd {

    @Command(aliases = { "hub" , "lobby" },desc = "Sends to hub", usage = "-")
     public static void hub(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            final RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            pl.message("§9§l>> §bTo hub! Here we go!");
            new BukkitRunnable() {
                @Override
            public void run() {
                    pl.send("H1");
                }
            }.runTaskLaterAsynchronously(RPICore.getInstance(), 40L);

        } else {
            sender.sendMessage("You are not a player");
        }
    }
}
