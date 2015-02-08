package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class RankAdminCmd extends RCommand {

    public RankAdminCmd() {
        super("perm");
        setRank(Rank.ADMIN);
        setAliases(Arrays.asList("rank"));
        setDescription("Set a player's rank");
        setArgsUsage("<Set/Unset> <Player> <Rank>");
        setMinimumArgs(2);
        setMaximumArgs(3);
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        CommandSender console = null;
        RPlayer player = pl;

        if (Bukkit.getPlayer(args[1]) != null) {
            RPlayer target = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[1]));
            if (args[0].equalsIgnoreCase("set")) {
                if (Rank.valueOf(args[2].toUpperCase()) != null) {
                    target.setRank(Rank.valueOf(args[2].toUpperCase()), true);
                    target.message("§6§l>> §eYour rank has been set to " + target.getRank().getName() + "§e.");
                    if (player != null) {
                        player.message("§6§l>> §eSet " + target.getRankedName(false) + "§e to " + target.getRank().getName());
                    } else {
                        console.sendMessage("Set " + target.getName() + " to " + target.getRank().getName());
                    }
                } else {
                    if (player != null) {
                        Rank[] ranks = Rank.values();
                        ArrayList<String> names = new ArrayList<>();

                        for (Rank rank : ranks) {
                            names.add(rank.getFormattedName());
                        }

                        player.message(MsgType.ERROR, "Available ranks: " + String.valueOf(StringUtils.join(names, "§7, ")));
                    } else {
                        Rank[] ranks = Rank.values();
                        ArrayList<String> names = new ArrayList<>();

                        for (Rank rank : ranks) {
                            names.add(rank.getRank());
                        }

                        console.sendMessage("Available ranks: " + String.valueOf(StringUtils.join(names, "§7, ")));
                    }
                }
            } else if (args[0].equalsIgnoreCase("unset")) {
                target.setRank(Rank.PLAYER, true);
                target.message("§6§l>> §eYour rank has been set to " + target.getRank().getName() + "§e.");
                if (player != null) {
                    player.message("§6§l>> §eUnset " + target.getRankedName(false) + " §e's rank.");
                } else {
                    console.sendMessage("Unset " + target.getName() + "'s rank");
                }
            } else {
                console.sendMessage("Arg 1 can only have 2 values [set] or [unset]");
            }
        } else {
            OfflineRPlayer target = new OfflineRPlayer(args[1]);
            if (!target.isNull()) {
                if (args[0].equalsIgnoreCase("set")) {
                    if (Rank.valueOf(args[2].toUpperCase()) != null) {
                        target.setRank(Rank.valueOf(args[2].toUpperCase()));
                        if (player != null) {
                            player.message("§6§l>> §eSet " + target.getRankedName(false) + "§e to " + target.getRank().getName());
                        } else {
                            console.sendMessage("Set " + target.getName() + " to " + target.getRank().getName());
                        }
                    } else {
                        if (player != null) {
                            Rank[] ranks = Rank.values();
                            ArrayList<String> names = new ArrayList<>();

                            for (Rank rank : ranks) {
                                names.add(rank.getFormattedName());
                            }

                            player.message(MsgType.ERROR, "Available ranks: " + String.valueOf(StringUtils.join(names, "§7, ")));
                        } else {
                            Rank[] ranks = Rank.values();
                            ArrayList<String> names = new ArrayList<>();

                            for (Rank rank : ranks) {
                                names.add(rank.getRank());
                            }

                            console.sendMessage("Available ranks: " + String.valueOf(StringUtils.join(names, "§7, ")));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("unset")) {
                    target.setRank(Rank.PLAYER);
                    if (player != null) {
                        player.message("§6§l>> §eUnset " + target.getRankedName(false) + " §e's rank in the database.");
                    } else {
                        console.sendMessage("Unset " + target.getName() + "'s rank in the database");
                    }
                } else {
                    console.sendMessage("Incorrect usageArg 1 can only have 2 values [set] or [unset]");
                }
            } else {
                if (player != null) {
                    player.message("Player not online and not found in the database, check your spelling!");
                } else {
                    console.sendMessage("Player not in database");
                }
            }
        }
    }
}
