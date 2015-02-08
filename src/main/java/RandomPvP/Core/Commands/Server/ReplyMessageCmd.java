package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
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
public class ReplyMessageCmd extends RCommand {

    public ReplyMessageCmd() {
        super("reply");
        setPlayerOnly(true);
        setDescription("Replies to a message");
        setArgsUsage("<Message>");
        setMinimumArgs(1);
        setAliases(Arrays.asList("r"));
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if (pl.getModule("LastMessage") != null) {
            RPlayer target = (RPlayer) pl.getModule("LastMessage").getData();
            pl.message("§9§l>> §7[" + pl.getRank().getColor() + "You§7] §9--> §7[" + target.getDisplayName(false) + "§7]§8: §2" + StringUtils.join(args, " "));
            target.message("§9§l>> §7[" + pl.getDisplayName(false) +  "§7] §9--> §7[" + target.getRank().getColor() + "You§7]§8: §2" + StringUtils.join(args, " "));

            if (target.getModule("LastMessage") != null) {
                target.getModule("LastMessage").setData(pl);
            }

        } else {
            pl.message(MsgType.ERROR, "There is no player to respond to!");
        }
    }
}
