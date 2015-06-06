package RandomPvP.Core.Util.Entity.Villager.ShopVillager;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Entity.Registration.EntityRegistration;
import RandomPvP.Core.Util.Entity.Villager.DisabledEntityVillager;
import RandomPvP.Core.Util.GUI.ShopGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ShopVillager extends DisabledEntityVillager implements Listener {

    private String name;
    private String gamename;
    private ShopGUI gui;
    private ItemStack icon;
    private boolean betaGame;

    public ShopVillager(Location loc, String name, ShopGUI gui, boolean betaGame) {
        super(((CraftWorld) loc.getWorld()).getHandle());
        setName(name);
        setName(getDefaultName());
        setGui(gui);
        setGamename(gamename);
        setBetaGame(betaGame);
        setIcon(icon);

        Bukkit.getServer().getPluginManager().registerEvents(this, RPICore.getInstance());

        EntityRegistration.spawnEntity(this, loc);
    }

    public String getDefaultName() {
        if(name != null && !name.equals("")) {
            return "§2§lSHOP NPC §7|| §4§l" + name.toUpperCase();
        } else {
            return "§2§lSHOP NPC §7|| §4§lUNKNOWN";
        }
    }

    public void setName(String n) {
        name = n;
        setCustomName(name);
        setCustomNameVisible(true);
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public ShopGUI getGui() {
        return gui;
    }

    public void setGui(ShopGUI gui) {
        this.gui = gui;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public boolean isBetaGame() {
        return betaGame;
    }

    public void setBetaGame(boolean betaGame) {
        this.betaGame = betaGame;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof Villager) {
            Villager vil = (Villager) e.getRightClicked();
            if(vil == getBukkitEntity()) {
                e.setCancelled(true);
                RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
                boolean open = true;
                {
                    if (betaGame) {
                        if (!pl.has(Rank.PREMIUM)) {
                            open = false;
                        }
                    }
                }
                if(open) {
                    gui.open(pl);
                }
            }
        }
    }

}
