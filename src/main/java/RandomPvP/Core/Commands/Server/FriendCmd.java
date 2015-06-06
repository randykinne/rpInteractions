package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Server.General.Friends.GUIs.FriendsGUI;

import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class FriendCmd extends RCommand {

    public FriendCmd() {
        super("friends");
        setPlayerOnly(true);
        setMaximumArgs(0);
        setAliases(Arrays.asList("f"));
    }

    @Override
    public void onCommand(RPlayer pl, String s, String[] strings) {
        pl.message(MsgType.ERROR, "Friends are currently disabled.");
        //pl.getPlayer().openInventory(new FriendsGUI(pl.getPlayer()).getInventory());
    }

}
