package RandomPvP.Core.Commands.Admin;


import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.RPStaff;
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
public class BroadcastAdminCmd {

    @Command(aliases = { "broadcast" }, desc = "Broadcasts a message to all online players", usage = "[message] - broadcasts to all players", min = 1)
    public static void broadcast(final CommandContext args, CommandSender sender) throws CommandException {

        String username;
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.getRank().has(Rank.ADMIN)) {
                username = pl.getRankedName(false);
            } else {
                throw new CommandException("§7You need §oAdmin §7to use this command.");
            }

        } else {
            username = "Console";
        }

        if (username != null) {
            RPStaff.sendAdminBroadcast(username + "§8: §7" + args.getJoinedStrings(0), true);
        }
    }
}
