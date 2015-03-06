package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.ChatColor;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ListCmd extends RCommand {

    public ListCmd() {
        super("list");
        setMaximumArgs(0);
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        StringBuilder sb = new StringBuilder();
        int online = PlayerManager.getInstance().getOnlinePlayers().size();
        int i = PlayerManager.getInstance().getOnlinePlayers().size();
        for(RPlayer p : PlayerManager.getInstance().getOnlinePlayers()) {
            i--;
            sb.append(p.getRankedName(false));
            if(i > 0) sb.append(ChatColor.GRAY + ", ");
        }
        pl.message(MsgType.INFO, "There are a total of " + ChatColor.DARK_RED + online + ChatColor.GRAY + " player(s) online: ");
        pl.message(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "> " + sb.toString());
    }
}
