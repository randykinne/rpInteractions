package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Server.General.Messages;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import RandomPvP.Core.Server.Punish.PunishmentType;
import RandomPvP.Core.Util.StringUtil;
import RandomPvP.Core.Util.TimeUtil;
import org.bukkit.ChatColor;

import java.util.Date;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class KickCmd extends RCommand {

    public KickCmd() {
        super("kick");
        setArgsUsage(" <Player> <Reason>");
        setPlayerOnly(true);
        setRank(Rank.MOD);
        setMinimumArgs(2);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        final RPlayer punished = PlayerManager.getInstance().getPlayer(args[0]);
        if(punished != null) {
            final String reason = StringUtil.join(args, " ", 1, args.length);
            Punishment pm = new Punishment(punished.getUUID(), pl.getUUID(), PunishmentType.KICK, reason, TimeUtil.dateFormat.format(new Date()), -1);
            {
                pm.save();
            }
            Messages.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(false) + ChatColor.GRAY + " kicked "
                    + punished.getDisplayName(false) + ChatColor.GRAY + " for " + ChatColor.BLUE + pm.getReason() + ChatColor.GRAY + ".");
            Messages.kickPlayerFromNetwork(punished, PunishmentManager.getInstance().generateMessage(pm));
        } else {
            pl.message(MsgType.ERROR, args[0] + " isn't online.");
        }
    }
}
