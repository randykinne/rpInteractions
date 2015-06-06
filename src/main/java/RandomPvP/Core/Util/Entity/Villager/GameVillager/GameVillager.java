package RandomPvP.Core.Util.Entity.Villager.GameVillager;

import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Entity.Registration.EntityRegistration;
import RandomPvP.Core.Util.Entity.Villager.DisabledEntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class GameVillager extends DisabledEntityVillager implements Listener {

    private RInventory inv;
    private String name;
    private String gamename;

    public GameVillager(Location loc, String gamename, RInventory inv) {
        super(((CraftWorld) loc.getWorld()).getHandle());
        setGamename(gamename);
        setName(getDefaultName());
        setInventory(inv);

        Bukkit.getServer().getPluginManager().registerEvents(this, RPICore.getInstance());

        EntityRegistration.spawnEntity(this, loc);
    }

    public String getDefaultName() {
        if(gamename != null) {
            return "§2§lGAME NPC §7|| §4§l" + gamename.toUpperCase();
        } else {
            return "§2§lGAME NPC §7|| §4§lUNKNOWN";
        }
    }

    public void setName(String n) {
        name = n;
        setCustomName(name);
        setCustomNameVisible(true);
    }

    public String getName() {
        return name;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public RInventory getInventory() {
        return inv;
    }

    public void setInventory(RInventory inv) {
        this.inv = inv;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof Villager) {
            Villager vil = (Villager) e.getRightClicked();
            if(vil == getBukkitEntity()) {
                e.setCancelled(true);
                e.getPlayer().openInventory(inv.getInventory());
            }
        }
    }

}
