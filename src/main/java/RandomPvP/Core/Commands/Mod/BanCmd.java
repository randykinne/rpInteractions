package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Punish.PunishmentType;
import RandomPvP.Core.Util.Broadcasts;
import RandomPvP.Core.Util.TimeUtil;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
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
public class BanCmd extends RCommand {

    public BanCmd() {
        super("ban");
        setArgsUsage("<Player> <Reason>");
        setPlayerOnly(true);
        setRank(Rank.MOD);
        setMinimumArgs(2);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        final OfflineRPlayer punished = new OfflineRPlayer(args[0]);
        if(!punished.isBanned()) {
            final String reason = StringUtils.join(args, " ", 1, args.length);
            Punishment pm = new Punishment(punished.getUUID(), pl.getUUID(), PunishmentType.PERMANENT_BAN, reason, TimeUtil.dateFormat.format(new Date()), -1);
            {
                pm.save();
            }
            Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, pl.getRankedName(false) + ChatColor.GRAY + " banned "
                    + punished.getRankedName(false) + ChatColor.GRAY + " for " + ChatColor.BLUE + pm.getReason() + ChatColor.GRAY + ".");
            if(Bukkit.getOfflinePlayer(punished.getUUID()).isOnline()) {
                Broadcasts.kickPlayerFromNetwork(PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(punished.getUUID())), PunishmentManager.getInstance().generateMessage(pm));
            }
        } else {
            pl.message(MsgType.ERROR, punished.getRankedName(false) + ChatColor.GRAY + " is already banned.");
        }
    }
}
