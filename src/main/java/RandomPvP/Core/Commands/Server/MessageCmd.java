package RandomPvP.Core.Commands.Server;

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
public class MessageCmd {

    @Command(aliases = { "msg", "m", "message", "w", "whisper", "tell" }, usage = "[player] [message]", desc = "Sends a player a message", min = 2)
    public static void message(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            Player player= Bukkit.getPlayer(args.getString(0));
            if (player != null) {
                RPlayer target = RPlayerManager.getInstance().getPlayer(player);
                pl.message("§dYou --> " + target.getDisplayName(false) + "§8: §2" + args.getJoinedStrings(1));
                target.message(pl.getDisplayName(false) +  " §a--> You" + "§8: §2" + args.getJoinedStrings(1));
            } else {
               throw new CommandException("§4Player not found!");
            }
        } else {
            sender.sendMessage("Must be player!");
        }
    }
}
