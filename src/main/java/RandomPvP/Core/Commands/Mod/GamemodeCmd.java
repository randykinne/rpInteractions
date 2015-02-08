package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.GameMode;

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
public class GameModeCmd extends RCommand {

    public GameModeCmd() {
        super("gamemode");
        setRank(Rank.BUILDER);
        setPlayerOnly(true);
        setDescription("Changes your gamemode");
        setArgsUsage("<0|1|2>");
        setAliases(Arrays.asList("gm"));
        setMaximumArgs(1);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {

        if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
            pl.getPlayer().setGameMode(GameMode.SURVIVAL);
        } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
            pl.getPlayer().setGameMode(GameMode.CREATIVE);
        } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
            pl.getPlayer().setGameMode(GameMode.ADVENTURE);
        } else {
            pl.message(MsgType.ERROR, "Gamemode not found!");
        }

        pl.message("§6§l>> §eGameMode set to §a" + pl.getPlayer().getGameMode().name() + "§7.");
    }
}
