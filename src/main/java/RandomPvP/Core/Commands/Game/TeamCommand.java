package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.ChatColor;

import java.util.Arrays;

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
public class TeamCommand extends RCommand {

    public TeamCommand() {
        super("team");
        setPlayerOnly(true);
        setAliases(Arrays.asList("t", "myteam"));
        setDescription("Shows your current team");
    }

    @Override
    public void onCommand(RPlayer player, String string, String[] args) {
        if (GameManager.getGame().isTeamBased()) {
            if (player.getTeam() != null) {
                player.message(MsgType.GAME, "Your current team: " + player.getTeam().getColor() + player.getTeam().getName());
            }
        } else {
            player.message(MsgType.ERROR, "Teams are not enabled for " + GameManager.getGame().getPrimaryColor() + GameManager.getGame().getName() + ChatColor.GRAY + ".");
        }
    }
}
