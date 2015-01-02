package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Game.Team.Team;
import RandomPvP.Core.Game.Team.TeamManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class TeamCommand implements Listener {

    @Command(aliases = "team", desc = "Sets your team.", usage = "<team> ", max = 1, min = 0)
    public static void team(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
            RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (GameManager.getGame().isTeamBased()) {
                if (args.argsLength() == 0) {
                    List<String> names = new ArrayList<>();
                    for (Team team : TeamManager.getTeams().values()) {
                        names.add(team.getColor() + team.getName());
                    }

                    throw new CommandException("You must select a team to join. Available: " + String.valueOf(Arrays.asList(names)).replace("[", "").replace("]", ""));
                } else if (TeamManager.getTeam(args.getString(0)) != null) {
                    try {
                        TeamManager.joinTeam(pl, TeamManager.getTeam(args.getString(0)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                throw new CommandException("Teams are not enabled for " + GameManager.getGame().getPrimaryColor() + GameManager.getGame().getName());
            }
        } else {
            sender.sendMessage("Only players can use this command!");
        }
    }
}
