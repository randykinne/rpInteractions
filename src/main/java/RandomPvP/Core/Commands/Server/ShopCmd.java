package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Game.GameState.GameState;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.Shop.GUI.MainGUI;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ShopCmd extends RCommand {

    public ShopCmd() {
        super("shop");
        setPlayerOnly(true);
        setMaximumArgs(0);
        setMinimumArgs(0);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(GameManager.getState() == GameState.LOBBY) {
            MainGUI.openGUI(pl);
        } else {
            pl.message(MsgType.ERROR, "The game must be in lobby to use this command.");
        }
    }

}
