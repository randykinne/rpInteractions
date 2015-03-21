package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.NetworkUtil;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RestartCmd extends RCommand {

    public RestartCmd() {
        super("restart");
        setPlayerOnly(true);
        setRank(Rank.ADMIN);
        setAliases(Arrays.asList("rs", "restat"));
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        Bukkit.getServer().broadcastMessage(MsgType.INFO.getPrefix() + "The server is now restarting.");
        NetworkUtil.restart();
    }

}
