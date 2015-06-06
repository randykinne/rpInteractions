package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Server.Punish.Punishment;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import RandomPvP.Core.Server.Punish.PunishmentType;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
            pl.message(" §f- §8Punished User: §7" + new OfflineRPlayer(pm.getPunished()).getRankedName(false));
            pl.message(" §f- §8Punishment Issuer: §7" + new OfflineRPlayer(pm.getIssuer()).getRankedName(false));
            pl.message(" §f- §8Punishment Type: §7" + pm.getType().getName());
            pl.message(" §f- §8Punish Date: §7" + pm.getTime());
            pl.message(" §f- §8Punishment Reason: §7" + pm.getReason());
            pl.message(" §f- §8Active: §7" + StringUtil.booleanToString(pm.isActive()));
        } else {
            OfflineRPlayer target = new OfflineRPlayer(args[0]);
            String alts = "There was an error fetching the player's alts.";
            {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `username` FROM `accounts` WHERE `ip`=?");
                    {
                        stmt.setString(1, target.getIP());
                    }
                    ResultSet res = stmt.executeQuery();
                    List<String> altsList = new ArrayList<String>();
                    {
                        while (res.next()) {
                            altsList.add(res.getString("username"));
                        }
                    }
                    if(altsList.size() > 0) {
                        alts = altsList.toString().replace("[", "").replace("]", "");
                    } else {
                        alts = "None found under IP.";
                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleErrorMessage(pl, ex);
                }
            }

            pl.message("§8§l>> §7Showing account information for " + target.getRankedName(false) + "§7...");
            pl.message(" §f- §8IP: §7" + target.getIP());
            pl.message("    §f- §8Alts: §7" + alts);
            pl.message(" §f- §8Currently Banned: §7" + StringUtil.booleanToString(PunishmentManager.getInstance().hasBanActive(target.getUUID())));
            pl.message(" §f- §8Currently Muted: §7" + StringUtil.booleanToString(PunishmentManager.getInstance().hasMuteActive(target.getUUID())));
            if(pl.isOnline()) {
                pl.message(" §f- §8Allowed to Fly: §7" + StringUtil.booleanToString(pl.getPlayer().getAllowFlight()));
            }
            pl.message(" §f- §8Punishments: ");

            if(PunishmentManager.getInstance().getPunishments(target.getUUID()).length != 0) {
                for (Punishment pm : PunishmentManager.getInstance().getPunishments(target.getUUID())) {
                    if (pm.getType() == PunishmentType.TEMPORARY_MUTE || pm.getType() == PunishmentType.TEMPORARY_BAN) {
                        pl.message("  §a#" + pm.getId() + ": §4" + pm.getType().getName() + " §7by "
                                + new OfflineRPlayer(pm.getIssuer()).getRankedName(false) + " §7for §9" + pm.getReason() + "§7 (§9" + pm.getEnd() + "§7)");
                    } else {
                        pl.message("  §a#" + pm.getId() + ": §4" + pm.getType().getName() + " §7by "
                                + new OfflineRPlayer(pm.getIssuer()).getRankedName(false) + " §7for §9" + pm.getReason() + "§7");
                    }
                }
            } else {
                pl.message("  §a§lNo punishments found on record.");
            }
        }
    }
}
