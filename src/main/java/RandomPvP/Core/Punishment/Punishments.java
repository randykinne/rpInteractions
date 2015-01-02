/*
* Punishments
* Copyright (C) 2014 Puzl Inc.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package RandomPvP.Core.Punishment;


import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.UUIDCache;
import RandomPvP.Core.RPICore;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Punishments {

    public static String formatKickMessage(String reason) {
        return "§cKicked from RandomPvP > §7" + reason;
    }

    static String mod = null;
    public static String formatBanMessage(String id, final UUID Moderator, String reason, String expiration) {
        if (expiration.equalsIgnoreCase("-1")) expiration = "Permanent";
        if (Moderator == null) {
            return "§a" + id + "§8: Banned §8> §7You have been banned for §f" + reason + "§7. \n §5Mod§8: §4AutoModerator§7.  §6Expires§8: " + expiration + "§7.";
        } else {
            new BukkitRunnable() {
                public void run() {
                    mod = UUIDCache.getName(Moderator);
                }
            }.runTaskAsynchronously(RPICore.getInstance());
            return "§a" + id + "§8: Banned §8> §7You have been banned for §f" + reason + "§7. \n §5Mod§8: " + new OfflineRPlayer(mod, Moderator).getRankedName(false) + "§7.  §6Expires§8: " + expiration + "§7.";
        }
    }
}
