package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Server.General.Messages;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class UnmuteCmd extends RCommand {

    public UnmuteCmd() {
        super("unmute");
        setRank(Rank.MOD);
        setPlayerOnly(true);
        setArgsUsage(" <Player>");
        setMaximumArgs(1);
        setMinimumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        final OfflineRPlayer target = new OfflineRPlayer(args[0]);
        if(target.isMuted()) {
            Punishment pm = PunishmentManager.getInstance().getActiveMute(target.getUUID());
            {
                pm.setActive(false);
                pm.save();
            }
            Messages.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(false) + ChatColor.GRAY + " unmuted "
                    + target.getRankedName(false) + ChatColor.GRAY + ".");
            if(Bukkit.getOfflinePlayer(target.getUUID()).isOnline()) {
                PlayerManager.getInstance().getPlayer(args[0]).message(MsgType.INFO, "You have been unmuted!");
            }
        } else {
            pl.message(MsgType.ERROR, target.getRankedName(false) + ChatColor.GRAY + " isn't currently muted.");
        }
    }
}
