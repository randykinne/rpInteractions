package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Punish.PunishmentType;
import org.bukkit.Bukkit;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class LookupCmd extends RCommand {

    public LookupCmd() {
        super("lookup");
        setPlayerOnly(true);
        setRank(Rank.MOD);
        setArgsUsage(" <Player | Punish ID>");
        setMinimumArgs(1);
        setMaximumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(args[0].startsWith("#")) {
            Punishment pm;
            {
                try {
                    pm = PunishmentManager.getInstance().getPunishment(Integer.valueOf(args[0].replace("#", "")));
                } catch (Exception ex) {
                    pl.message(MsgType.ERROR, "Invalid punishment ID.");
                    return;
                }
            }
            pl.message("§8§l>> §7Showing punishment information for punishment §9" + args[0] + "§7...");
            pl.message(" §f- §8Punished User: §7" + new OfflineRPlayer(Bukkit.getPlayer(pm.getPunished()).getName()).getRankedName(false));
            pl.message(" §f- §8Punishment Issuer: §7" + new OfflineRPlayer(Bukkit.getPlayer(pm.getIssuer()).getName()).getRankedName(false));
            pl.message(" §f- §8Punishment Type: §7" + pm.getType());
            pl.message(" §f- §8Punish Date: §7" + pm.getTime());
            pl.message(" §f- §8Punishment Reason: §7" + pm.getReason());
            pl.message(" §f- §8Active: §7" + pm.isActive());
        } else {
            OfflineRPlayer target = new OfflineRPlayer(args[0]);
            pl.message("§8§l>> §7Showing account information for " + target.getRankedName(false) + "§7...");
            pl.message(" §f- §8IP: §7" + target.getIP());
            pl.message(" §f- §8Currently Banned: §7" + PunishmentManager.getInstance().hasBanActive(target.getUUID()));
            pl.message(" §f- §8Currently Muted: §7" + PunishmentManager.getInstance().hasMuteActive(target.getUUID()));
            pl.message(" §f- §8Punishments: ");
            for(Punishment pm : PunishmentManager.getInstance().getPunishments(target.getUUID())) {
                if(pm.getType() == PunishmentType.TEMPORARY_MUTE || pm.getType() == PunishmentType.TEMPORARY_BAN) {
                    pl.message("  §a#" + pm.getId() + ": §4" + pm.getType().getName() + " §7by "
                            + new OfflineRPlayer(Bukkit.getPlayer(pm.getIssuer()).getName()).getRankedName(false) + " §7for §9" + pm.getReason() + "§7 (§9" + pm.getEnd() + "§7)");
                } else {
                    pl.message("  §a#" + pm.getId() + ": §4" + pm.getType().getName() + " §7by "
                            + new OfflineRPlayer(Bukkit.getPlayer(pm.getIssuer()).getName()).getRankedName(false) + " §7for §9" + pm.getReason() + "§7");
                }
            }
        }
    }
}
