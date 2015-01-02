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

package RandomPvP.Core.Punishment.command;

import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.UUIDCache;
import RandomPvP.Core.Punishment.PunishmentManager;
import RandomPvP.Core.Punishment.Punishments;
import RandomPvP.Core.Punishment.Util;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.DateUtil;
import RandomPvP.Core.Util.RPStaff;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private PunishmentManager manager;

    public CommandHandler(PunishmentManager manager) {
        this.manager = manager;
    }

    private UUID id;
    private void setId(UUID id) {
        this.id = id;
    }
    private UUID getId() {
        return id;
    }
    private UUID getUUID(final String name) {
        Bukkit.getServer().getScheduler().runTaskAsynchronously(RPICore.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                setId(UUIDCache.getUUID(name));
            }
        });

        return getId();
    }
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args) {
        String label = cmd.getLabel();
        UUID admin = (cs instanceof Player) ? (UUIDCache.getUUID(cs.getName())) : null;
        final String mod = (cs instanceof  Player) ? (new OfflineRPlayer(cs.getName(), getUUID(cs.getName())).getRankedName(false)) : "§4AutoModerator";

        if(label.equalsIgnoreCase("kick")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length > 0) {
                    final String name = args[0];
                    Player player = Bukkit.getPlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name+ " not found! Double check your spelling ;)");
                        return true;
                    }

                    String reason = null;
                    if (args.length >= 2) {
                        reason = StringUtils.join(args, " ", 1, args.length);
                    }

                    final PunishmentManager.Punishment punishment = manager.addPunishment(
                            PunishmentManager.PunishmentType.KICK,
                            UUIDCache.getUUID(player.getName()),
                            admin,
                            System.currentTimeMillis(),
                            manager.PUNISHMENT_EXPIRED,
                            manager.getServer(),
                            reason
                    );

                    player.kickPlayer(Punishments.formatKickMessage(punishment.getMessage()));
                    new BukkitRunnable() {
                        public void run() {
                            RPStaff.sendStaffMessage(mod + "§8 > " + punishment.getType().getName() + "§8 > " + new OfflineRPlayer(name, getUUID(name)).getRankedName(false) + " §8- §b§n" + punishment.getReason(), false);
                        }
                    }.runTaskAsynchronously(RPICore.getInstance());
                } else {
                    throwNewCommandException(cs, "Usage: /kick <player> [reason]");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        } else if(label.equalsIgnoreCase("ban")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length > 0) {
                    String name = args[0];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name + " not found! Double check your spelling ;)");
                        return true;
                    }

                    if (manager.hasActivePunishment(UUIDCache.getUUID(player.getName()), PunishmentManager.PunishmentType.BAN) != null) {
                        throwNewCommandException(cs, "Player has already been banned!");
                        return true;
                    }

                    String reason = null;
                    long length = manager.PUNISHMENT_EXPIRE_NEVER;
                    reason = StringUtils.join(args, " ", 1, args.length);

                    PunishmentManager.Punishment punishment = manager.addPunishment(
                            PunishmentManager.PunishmentType.BAN,
                            UUIDCache.getUUID(player.getName()),
                            admin,
                            System.currentTimeMillis(),
                            length,
                            manager.getServer(),
                            reason
                    );

                    if (player.isOnline()) {
                        ((Player)player).kickPlayer(Punishments.formatKickMessage(punishment.getReason()));
                    }

                    RPStaff.sendStaffMessage(mod + "§8 > " + punishment.getType().getName() + "§8 > " + new OfflineRPlayer(name, getUUID(name)).getRankedName(false) + " §8- §b§n" + punishment.getReason(), false);
                } else {
                    throwNewCommandException(cs, "Usage: /ban <player> [length] [reason]");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        } else if(label.equalsIgnoreCase("tempban")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length > 0) {
                    String name = args[0];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name + " not found! Double check your spelling ;)");
                        return true;
                    }

                    if (manager.hasActivePunishment(UUIDCache.getUUID(player.getName()), PunishmentManager.PunishmentType.BAN) != null) {
                        throwNewCommandException(cs, "Player has already been banned!");
                        return true;
                    }

                    Long timeExpires = Long.valueOf(DateUtil.getTimeStamp(args[1]));
                    String reason = null;
                    String formatExpires = DateUtil.formatDateDiff(timeExpires.longValue());
                    timeExpires = Long.valueOf(timeExpires.longValue() / 1000L);;
                    reason = StringUtils.join(args, " ", 1, args.length);

                    PunishmentManager.Punishment punishment = manager.addPunishment(
                            PunishmentManager.PunishmentType.BAN,
                            UUIDCache.getUUID(player.getName()),
                            admin,
                            System.currentTimeMillis(),
                            timeExpires,
                            manager.getServer(),
                            reason
                    );

                    if (player.isOnline()) {
                        ((Player)player).kickPlayer(Punishments.formatKickMessage(punishment.getReason()));
                    }

                    RPStaff.sendStaffMessage(mod + "§8 > " + punishment.getType().getName() + "§8 > " + new OfflineRPlayer(name, getUUID(name)).getRankedName(false) + " §8- §b§n" + punishment.getReason(), false);
                } else {
                    throwNewCommandException(cs, "Usage: /ban <player> [length] [reason]");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        } else if(label.equalsIgnoreCase("mute")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length > 0) {
                    String name = args[0];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name + " not found! Double check your spelling ;)");
                        return true;
                    }

                    if (manager.hasActivePunishment(UUIDCache.getUUID(player.getName()), PunishmentManager.PunishmentType.MUTE) != null) {
                        throwNewCommandException(cs, "Player has already been muted!");
                        return true;
                    }

                    String reason = null;
                    long length = manager.PUNISHMENT_EXPIRE_NEVER;
                    reason = StringUtils.join(args, " ", 1, args.length);

                    PunishmentManager.Punishment punishment = manager.addPunishment(
                            PunishmentManager.PunishmentType.MUTE,
                            UUIDCache.getUUID(player.getName()),
                            admin,
                            System.currentTimeMillis(),
                            length,
                            manager.getServer(),
                            reason
                    );

                    if (player.isOnline()) {
                        RPlayerManager.getInstance().getPlayer((Player) player).warn(reason);
                    }
                    RPStaff.sendStaffMessage(mod + "§8 > " + punishment.getType().getName() + "§8 > " + new OfflineRPlayer(name, getUUID(name)).getRankedName(false) + " §8- §b§n" + punishment.getReason(), false);
                } else {
                    throwNewCommandException(cs, "Usage: /mute <player> [reason]");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        } else if(label.equalsIgnoreCase("unban")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length == 1) {
                    String name = args[0];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name + " not found! Double check your spelling ;)");
                        return true;
                    }

                    PunishmentManager.Punishment punishment;
                    if ((punishment = manager.hasActivePunishment(UUIDCache.getUUID(player.getName()), PunishmentManager.PunishmentType.BAN)) != null) {
                        punishment.expire();
                        RPStaff.sendStaffMessage(mod + " §aunbanned " + new OfflineRPlayer(name, getUUID(name)).getRankedName(false) + "§a.", false);
                    } else {
                        throwNewCommandException(cs, "Player is not currently banned!");
                    }
                } else {
                    throwNewCommandException(cs, "Usage: /unban <player>");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        } else if(label.equalsIgnoreCase("unmute")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length == 1) {
                    String name = args[0];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name + " not found! Double check your spelling ;)");
                        return true;
                    }

                    PunishmentManager.Punishment punishment;
                    if ((punishment = manager.hasActivePunishment(UUIDCache.getUUID(player.getName()), PunishmentManager.PunishmentType.MUTE)) != null) {
                        punishment.expire();
                        RPStaff.sendStaffMessage(mod + " §aunmuted " + new OfflineRPlayer(name, getUUID(name)).getRankedName(false) + "§a.", false);
                    } else {
                        throwNewCommandException(cs, "Player is not currently muted!");
                    }
                } else {
                    throwNewCommandException(cs, "Usage: /unmute <player> [reason]");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        } else if(label.equalsIgnoreCase("history")) {
            if ((cs instanceof Player && RPlayerManager.getInstance().getPlayer((Player) cs).isStaff()) || !(cs instanceof Player) ) {
                if (args.length == 1) {
                    String name = args[0];
                    OfflinePlayer player = Bukkit.getOfflinePlayer(name);

                    if (player == null) {
                        throwNewCommandException(cs, "Player " + name + " not found! Double check your spelling ;)");
                        return true;
                    }

                    cs.sendMessage("§4History for: " + player.getName());

                    for (PunishmentManager.Punishment punishment : manager.getAllPunishmentsFor(UUIDCache.getUUID(player.getName()))) {
                        cs.sendMessage(
                                ChatColor.GOLD +
                                "(" + punishment.getId() + ") " +
                                ChatColor.GRAY +
                                Util.formatTimestamp(punishment.getCreated()) +
                                ": " +
                                ChatColor.WHITE +
                                punishment.getMessage() +
                                " by " +
                                ChatColor.GRAY +
                                (punishment.getAdmin() == null ? "§4Auto Moderator" : new OfflineRPlayer(name, getUUID(name)).getRankedName(false))
                        );
                    }
                } else {
                    throwNewCommandException(cs, "Usage: /history <player>");
                }
            } else {
                throwNewCommandException(cs, "You need Mod to use this command.");
            }
        }
        return true;
    }

    private static void throwNewCommandException(CommandSender sender, String message) {
        sender.sendMessage("§4§l>> §7" + message);
    }
}
