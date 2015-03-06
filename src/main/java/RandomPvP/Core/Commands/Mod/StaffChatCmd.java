package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.Broadcasts;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

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
public class StaffChatCmd extends RCommand {

    public StaffChatCmd() {
        super("sc");
        setAliases(Arrays.asList("mc", "staffchat", "modchat"));
        setDescription("Staff chat");
        setPlayerOnly(true);
        setMaximumArgs(0);
        setRank(Rank.MOD);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        pl.getStaff().toggleStaffChat();
    }
}
