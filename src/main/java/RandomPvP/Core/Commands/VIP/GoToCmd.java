package RandomPvP.Core.Commands.VIP;

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
public class GoToCmd {

    @Command(aliases = { "goto", "vipgo", "vipgoto" }, usage = "<server>", desc = "Takes you where you want to go!", min = 1)
    public static void goTo(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);

            if (pl.isVIP()) {
                String server = "H1";
                switch (args.getString(0)) {
                    case "PvPAcademy": server = "PA1"; break;
                    case "pvpacademy": server = "PA1"; break;
                    case "pvpac": server = "PA1"; break;
                    case "PA": server = "PA1"; break;
                    case "PA1": server = "PA1"; break;

                    case "UltraHardCore": server = "UHC1"; break;
                    case "UHC": server = "UHC1"; break;

                    case "GhostCraft": server = "GC1"; break;
                    case "GC": server = "GC1"; break;
                    case "Ghost": server = "GC1"; break;
                    case "gh": server = "GC1"; break;
                    case "gho": server = "GC1"; break;

                    default:
                }
                pl.send(server);
            }

        } else {
            throw new CommandException("You must be a player to run this command!");
        }
    }
}
