package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.Module.IModule;
import RandomPvP.Core.Util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
public class MessageCmd extends RCommand {

    public MessageCmd() {
        super("message");
        setPlayerOnly(true);
        setAliases(Arrays.asList("msg", "m", "w", "whisper", "tell"));
        setDescription("Sends a message to a player");
        setArgsUsage("<Player> <Message>");
        setMinimumArgs(2);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(pl.isMuted()) {
            pl.message(MsgType.ERROR, "You cannot use message while muted.");
            return;
        }
        if(!pl.getToggles().isPlayerMessages()) {
            pl.message(MsgType.ERROR, "You must toggle player messages on to use this.");
            return;
        }

        Player player= Bukkit.getPlayer(args[0]);
        if (player != null) {
            RPlayer target = PlayerManager.getInstance().getPlayer(player);

            if(!target.getToggles().isPlayerMessages()) {
                pl.message(MsgType.ERROR, "That player doesn't have player messages enabled.");
                return;
            }
            if(target.isMuted()) {
                pl.message(MsgType.ERROR, "That player is currently muted.");
                return;
            }

            pl.message("§9§l>> §7[" + pl.getRank().getColor() + "You§7] §9--> §7[" + target.getDisplayName(false) + "§7]§8: §2" + StringUtil.join(args, " ", 1, args.length));
            target.message("§9§l>> §7[" + pl.getDisplayName(false) +  "§7] §9--> §7[" + target.getRank().getColor() + "You§7]§8: §2" + StringUtil.join(args, " ", 1, args.length));

            if (pl.getModule("LastMessage") != null) {
                pl.getModule("LastMessage").setData(target);
            } else {
                pl.addModule(new IModule("LastMessage", target));
            }

            if (target.getModule("LastMessage") != null) {
                target.getModule("LastMessage").setData(pl);
            } else {
                target.addModule(new IModule("LastMessage", pl));
            }
        } else {
            pl.message(MsgType.ERROR, "§4Player not found!");
        }
    }
}
