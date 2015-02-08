package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
public class PingCmd extends RCommand {

    public PingCmd() {
        super("ping");
        setPlayerOnly(true);
        setDescription("What's your ping?");
        setArgsUsage("[player]");
    }

    @Override
    public void onCommand(RPlayer player, String string, String[] args) {
        if (args.length > 0) {
            if (Bukkit.getPlayer(args[0]) != null) {

                try {
                    int ping = RPICore.getInstance().list.getPlayerPing(Bukkit.getPlayer(args[0]));
                    player.message("§9§l>> " +
                            PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0])).getRankedName(false) +
                            "§b's current ping is " +
                            getColor(ping) +
                            ping +
                            "ms");
                } catch (IllegalAccessException e) {
                    player.message(MsgType.ERROR, "Unable to retrieve player's ping. Ping OP! :(");
                }
            } else {
                player.message(MsgType.ERROR, args[0] + " is not online! Did you spell it correctly?");
            }
        } else {
            try {
                int ping = RPICore.getInstance().list.getPlayerPing(player.getPlayer());
                player.message("§9§l>> §bYour current ping is " + getColor(ping) + ping + "ms");
            } catch (IllegalAccessException e) {
                player.message(MsgType.ERROR, "Unable to retrieve your ping. Ping OP! :(");
            }
        }
    }

    private ChatColor getColor(int ping) {
        if (ping <= 125) {
            return ChatColor.GREEN;
        } else if (ping > 125 && ping <= 250) {
            return ChatColor.YELLOW;
        } else {
            return ChatColor.RED;
        }
    }
}
