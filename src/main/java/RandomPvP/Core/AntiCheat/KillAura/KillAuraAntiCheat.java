package RandomPvP.Core.AntiCheat.KillAura;

import RandomPvP.Core.AntiCheat.Cheat;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.event.Listener;

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
public class KillAuraAntiCheat implements Cheat, Listener {

    @Override
    public String getName() {
        return "Aura Test";
    }

    @Override
    public boolean isPvPRelated() {
        return true;
    }

    @Override
    public void check(final RPlayer pl) {
        //TODO Implement this nicely
        /*
        final NPCFactory factory = new NPCFactory(RPICore.getInstance());
        Player player = pl.getPlayer();
        Location loc1;
        Location loc2;
        Location loc3;
        Location loc4;

        loc1 = player.getLocation().add(1, 0, 0);
        loc2 = player.getLocation().add(0, 0, 1);
        loc3 = player.getLocation().subtract(1, 0, 0);
        loc4 = player.getLocation().subtract(0, 0, 1);

        final NPC npc1 = factory.spawnHumanNPC(1, loc1, NPCProfile.loadProfile("1", "turqmelon"));
        final NPC npc2 = factory.spawnHumanNPC(2, loc2, NPCProfile.loadProfile("2", "turqmelon"));
        final NPC npc3 = factory.spawnHumanNPC(3, loc3, NPCProfile.loadProfile("3", "turqmelon"));
        final NPC npc4 = factory.spawnHumanNPC(4, loc4, NPCProfile.loadProfile("4", "turqmelon"));

        new BukkitRunnable() {
            public void run() {
                factory.despawnAll();
            }
        }.runTaskLaterAsynchronously(RPICore.getInstance(), 10L);

        new BukkitRunnable() {
            public void run() {
                RPStaff.sendStaffMessage("§2§l>> " + pl.getRankedName(false) + " §9hit " + factory.getHitNPCs().size() + "/4 entities on §3" + getName() + "§9 in §e§n" + Bukkit.getServerName(),  true);
            }
        }.runTaskLaterAsynchronously(RPICore.getInstance(), 60L);
        */



    }
}
