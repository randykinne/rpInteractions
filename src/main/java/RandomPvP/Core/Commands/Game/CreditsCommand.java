package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.Bukkit;

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
public class CreditsCommand extends RCommand {

    public CreditsCommand() {
        super("credits");
        setPlayerOnly(true);
        setAliases(Arrays.asList("c", "cr"));
        setDescription("Your credits");
        setArgsUsage("-");
    }

    @Override
    public void onCommand(RPlayer player, String string, String[] args) {
        if (args.length > 0) {
            if (Bukkit.getPlayer(args[0]) != null) {
                RPlayer target = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0]));
                player.message(MsgType.NETWORK, target.getRankedName(false) + "§b's Credits: " + target.getCredits());
            } else {
                player.message(MsgType.ERROR, "Player not found!");
            }
        } else {
            player.message(MsgType.NETWORK, "Credits: " + player.getCredits());
        }
    }
}
