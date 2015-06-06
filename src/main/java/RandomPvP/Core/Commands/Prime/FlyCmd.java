package RandomPvP.Core.Commands.Prime;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Server.General.Shop.ShopManager;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class FlyCmd extends RCommand {

    public FlyCmd() {
        super("fly");
        setPlayerOnly(true);
        setRank(Rank.PRIME);
        setDescription("Toggle fly mode.");
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(GameManager.getGame().allowDefaultFlight()) {
            if (pl.getPlayer().getAllowFlight()) {
                pl.getPlayer().setAllowFlight(false);
                pl.getPlayer().setFlying(false);
                pl.message(MsgType.INFO, "Disabled flight.");
            } else {
                pl.getPlayer().setAllowFlight(true);
                pl.getPlayer().setFlying(true);
                pl.message(MsgType.INFO, "Enabled flight.");
            }
        } else {
            pl.message(MsgType.ERROR, "This command is not enabled here.");
        }
    }
}
