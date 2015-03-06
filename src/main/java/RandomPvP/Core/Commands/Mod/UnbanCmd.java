package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Util.Broadcasts;
import org.bukkit.ChatColor;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class UnbanCmd extends RCommand {

    public UnbanCmd() {
        super("unban");
        setRank(Rank.MOD);
        setPlayerOnly(true);
        setArgsUsage(" <Player>");
        setMaximumArgs(1);
        setMinimumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        final OfflineRPlayer target = new OfflineRPlayer(args[0]);
        if(target.isBanned()) {
            Punishment pm = PunishmentManager.getInstance().getActiveBan(target.getUUID());
            {
                pm.setActive(false);
                pm.save();
            }
            Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(false) + ChatColor.GRAY + " unbanned "
                    + target.getRankedName(false) + ChatColor.GRAY + ".");
        } else {
            pl.message(MsgType.ERROR, target.getRankedName(false) + ChatColor.GRAY + " isn't currently banned.");
        }
    }
}
