package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;

import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class UngroundCmd extends RCommand {

    public UngroundCmd() {
        super("fixme");
        setAliases(Arrays.asList("unground", "inground", "fixground"));
        setPlayerOnly(true);
        setMaximumArgs(0);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        pl.getPlayer().teleport(pl.getLocation());
        pl.message(MsgType.INFO, "Successfully attempted to un-ground you for other players.");
    }

}
