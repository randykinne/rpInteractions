package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

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
                player.message("§9§l>> " +
                        PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0])).getRankedName(false) +
                        "§b's current ping is " +
                        getColor(((CraftPlayer)Bukkit.getPlayer(args[0])).getHandle().ping) +
                        ((CraftPlayer)Bukkit.getPlayer(args[0])).getHandle().ping +
                        "ms");
            } else {
                player.message(MsgType.ERROR, args[0] + " is not online! Did you spell it correctly?");
            }
        } else {
            player.message("§9§l>> " +
                    "§bYour current ping is " +
                    getColor(((CraftPlayer)player.getPlayer()).getHandle().ping) +
                    ((CraftPlayer)player.getPlayer()).getHandle().ping +
                    "ms");
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
