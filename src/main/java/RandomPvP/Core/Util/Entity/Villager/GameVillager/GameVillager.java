package RandomPvP.Core.Util.Entity.Villager.GameVillager;

import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Entity.Registration.RegisterEntity;
import RandomPvP.Core.Util.Entity.Villager.CustomEntityVillager;
import net.minecraft.server.v1_7_R4.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class GameVillager extends CustomEntityVillager implements Listener {

    protected CustomEntityVillager v;
    private Inventory inv;
    private String name;
    private Location loc;
    private String gamename;

    public GameVillager(Location loc, String gamename, Inventory inv) {
        super(((CraftWorld) loc.getWorld()).getHandle());
        v = this;
        setLocation(loc);
        setGamename(gamename);
        setName(getDefaultName());
        setInventory(inv);

        Bukkit.getServer().getPluginManager().registerEvents(this, RPICore.getInstance());

        new RegisterEntity().addCustomEntity(this.getClass(), getName(), 120);
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public String getDefaultName() {
        if(gamename != null) {
            return "§2§lGAME NPC §7| " + gamename;
        } else {
            return "§2§lGAME NPC §7| §6UNKNOWN";
        }
    }

    public void setName(String n) {
        name = n;
        v.setCustomName(name);
        v.setCustomNameVisible(true);
    }

    public String getName() {
        return name;
    }

    public void setLocation(Location l) {
        loc = l;
        v.setLocation(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
    }

    public Location getLocation() {
        return loc;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public Inventory getInventory() {
        return inv;
    }

    public void setInventory(Inventory inv) {
        this.inv = inv;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof Villager) {
            Villager vil = (Villager) e.getRightClicked();
            if(vil.getCustomName().equals(getName())) {
                e.setCancelled(true);
                e.getPlayer().openInventory(inv);
            }
        }
    }

}
