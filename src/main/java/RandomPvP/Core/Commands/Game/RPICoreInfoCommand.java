package RandomPvP.Core.Commands.Game;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

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
public class RPICoreInfoCommand extends RCommand {

    public RPICoreInfoCommand() {
        super("game");
        setPlayerOnly(true);
        setDescription("Core RandomPvP Information");
        setAliases(Arrays.asList("ver", "version", "rpi", "rpicore", "core", "?", "pl"));
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        PluginDescriptionFile pdf = new RPICoreInfoCommand().getDesc();
        pl.message("§8§l>> §cRunning §4§l" + pdf.getName() + " §cbuild " + pdf.getVersion());
        pl.message("§8§l>> §cDeveloped by §4RPDEV§c.");
    }

    public PluginDescriptionFile getDesc() {
        return RPICore.getInstance().getDescription();
    }
}
