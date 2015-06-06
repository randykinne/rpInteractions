package RandomPvP.Core.Player;

import RandomPvP.Core.RPICore;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
public class RStaff {

    private RPlayer pl;
    private boolean hasSTFUEnabled = false;
    private boolean hasStaffChatToggled = false;

    public RStaff(RPlayer pl) {
        this.pl = pl;
    }

    public boolean hasStaffChatToggled() {
        return hasStaffChatToggled;
    }

    public void toggleStaffChat() {
        if (hasStaffChatToggled) {
            pl.message("§6§l>> §Toggled staff chat §c§lOFF§e.");
            hasStaffChatToggled = false;
        } else {
            pl.message("§6§l>> §Toggled staff chat §a§lON§e.");
            hasStaffChatToggled = true;
        }
    }

    public void toggleSTFU() {
        if (hasSTFUEnabled) {
            pl.message("§6§l>> §eTurned §c§lOFF §estfu mode.");
            pl.message("§6§l>> §6You can now see all staff messages.");
            getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_PLING, 2L, 1L);
            hasSTFUEnabled = false;
        } else {
            pl.message("§6§l>> §eTurned §a§lON §estfu mode.");
            pl.message("§6§l>> §6It'll automatically disable when you switch servers.");
            getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_PLING, 2L, 1L);
            hasSTFUEnabled = true;
            new BukkitRunnable() {
                public void run() {
                    if (hasSTFUEnabled()) {
                        pl.message("§6§l>> §fYou have staff messages §c§lOFF§7.");
                        pl.message("§6§l>> §e/stfu §7to turn them §7back on.");
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(RPICore.getInstance(), 2400L, 2400L);

        }
    }

    public boolean hasSTFUEnabled() {
        return hasSTFUEnabled;
    }

    public Player getPlayer() { return pl.getPlayer(); }
}
