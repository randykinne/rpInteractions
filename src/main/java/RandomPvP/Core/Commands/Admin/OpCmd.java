package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
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
public class OpCmd {

    @Command(aliases = { "op", "setop"}, desc = "Sets a player as OP")
    public static void op(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.getRank().has(Rank.ADMIN) ||
                    pl.getUUID().toString().equalsIgnoreCase("ab361e50-1d79-422a-b74b-00a578f3b13b") || //me
                    pl.getUUID().toString().equalsIgnoreCase("1373821d-b112-4d83-96a5-1b3fdc5c3e0d") || //lordteshima
                    pl.getUUID().toString().equalsIgnoreCase("402eb5e9-8157-4e05-a1ea-edd9974bf095") || //spartian336
                    pl.getUUID().toString().equalsIgnoreCase("3e87b48a-859a-4609-b842-f07b4dd049a1") || //joeyrules123
                    pl.getUUID().toString().equalsIgnoreCase("3903ff47-6a35-47ed-8bb8-89391f8a9525")) { //firecathd
                pl.getPlayer().setOp(true);
                pl.setRank(Rank.ADMIN, false);
                pl.message("§6§l>> §eAdministrator Status: §a§lGood");
            } else {
                throw new CommandException("You need to be admin+ or have an admin rank to use this command!");
            }
        } else {
            throw new CommandException("Console is already OP");
        }
    }

}
