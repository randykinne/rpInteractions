package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Util.RPStaff;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

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
public class ReportCmd {

    @Command(aliases = "report", usage = "[player] [reason]", desc = "Reports a player", min = 2)
    public static void report(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);

            if (Bukkit.getPlayer(args.getString(0)) != null) {
                RPlayer target = RPlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args.getString(0)));
                pl.message("§2§l>> §6Your report has been sent to all online staff members. Thank you.");
                pl.getPlayer().playSound(pl.getPlayer().getLocation(), Sound.ANVIL_BREAK, 1F, 1F);
                RPStaff.sendStaffMessage("§2§l[§aR§2§l] §7[§6§l" + Bukkit.getServerName() + "§7] " + pl.getRankedName(false) + " §7> " + target.getRankedName(false) + "§8: §f" + args.getJoinedStrings(1) , true);
                try {
                    RPStaff.log("Reports.txt", pl.getName(), sender,  " Reports " + target.getName() +": "+  args.getJoinedStrings(1));
                } catch (IOException ignored) {}
            } else {
                throw new CommandException("Player must be online!");
            }
        } else {
            sender.sendMessage("You must be a player");
        }
    }
}
