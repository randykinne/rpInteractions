package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Sound;
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
public class RDPVPCmd {

    @Command(aliases = { "rdpvp", "acknowledge" }, desc = "Declares a warning as acknowledged", usage = "- Acknowledges a warning")
    public static void rdpvp(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.hasWarningPending()) {
                userAcknowledgedWarning(pl);
            } else {
                userHasNoWarningPending(pl);
            }
        } else {
            throw new CommandException("Console cannot have warnings pending!");
        }
    }

    private static void userAcknowledgedWarning(RPlayer pl) {
        pl.message("§6§l>> §eWarning acknowledged. Thank you, please have a nice day.");
        pl.getPlayer().playSound(pl.getPlayer().getLocation(), Sound.ANVIL_BREAK, 1F, 1F);
        pl.setHasWarningPending(false);
    }
    private static void userHasNoWarningPending(RPlayer pl) {
        pl.message("§4§l>> §cNo warning pending! How can you acknowledge a warning that doesn't exist?");
    }
}
