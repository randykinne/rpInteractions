package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import org.bukkit.scheduler.BukkitRunnable;

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
public class HubCmd extends RCommand {

    public HubCmd() {
        super("hub");
        setPlayerOnly(true);
        setAliases(Arrays.asList("lobby"));
        setDescription("To the lobby!");
    }

    @Override
    public void onCommand(final RPlayer pl, String string, String[] args) {
        pl.message("§9§l>> §bTo hub! Here we go!");
        new BukkitRunnable() {
            @Override
            public void run() {
                pl.send("H1");
            }
        }.runTaskLaterAsynchronously(RPICore.getInstance(), 40L);
    }
}
