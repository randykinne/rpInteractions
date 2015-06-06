package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.Bukkit;

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
public class WhereAmICommand extends RCommand {

    public WhereAmICommand() {
        super("whereami");
        setPlayerOnly(true);
        setDescription("Shows your current location on the network");
    }

    @Override
    public void onCommand(RPlayer player, String string, String[] args) {
        player.message(MsgType.GAME, "You are playing " +
                GameManager.getGame().getPrimaryColor() +
                GameManager.getGame().getName() +
                "§7 on " +
                GameManager.getGame().getPrimaryColor() +
                Bukkit.getServerName() + "§7.");
    }
}
