package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.ServerToggles;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
public class SilenceChatCmd extends RCommand {

    public SilenceChatCmd() {
        super("silencechat");
        setRank(Rank.MOD);
        setAliases(Arrays.asList("globalmute", "muteall", "silence"));
        setDescription("Prevents all non-VIP+ players from speaking");
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        for (RPlayer p : PlayerManager.getInstance().getOnlinePlayers()) {
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 1L, 1L);
        }

        if (ServerToggles.isChatEnabled()) {
            ServerToggles.setChatEnabled(false);
            Bukkit.broadcastMessage("§4§l>> §c§lSILENCE!");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.broadcastMessage("§4§l>> §8(§7§oGlobal Mute §c§oEnabled§8)");
                    Bukkit.broadcastMessage("§4§l>> §8(§7§oAll non-staff members have been silenced§8)");
                }
            }.runTaskLater(RPICore.getInstance(), 15L);
        } else {
            ServerToggles.setChatEnabled(true);
            Bukkit.broadcastMessage("§2§l>> §a§lYOU MAY SPEAK AGAIN!");
            new BukkitRunnable() {
                public void run() {
                    Bukkit.broadcastMessage("§2§l>> §8(§7§oGlobal Mute §a§oDisabled§8)");
                    Bukkit.broadcastMessage("§2§l>> §8(§7§oChat has been returned to normal state§8)");
                }
            }.runTaskLater(RPICore.getInstance(), 15L);
        }

    }
}
