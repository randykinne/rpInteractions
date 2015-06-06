package RandomPvP.Core.Player.Inventory;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public abstract class Button implements Listener {

    private RInventory in;
    private String newName = getName();
    private Material newMaterial = getMaterial();
    private int newAmount = getAmount();
    private List<String> newDesc = getDescription();
    private int newLoc = getLocation();
    private boolean newCloseOnClick = closeOnClick();

    public abstract String getName();
    public abstract Material getMaterial();
    public abstract int getAmount();
    public abstract List<String> getDescription();
    public abstract int getLocation();
    public abstract boolean closeOnClick();
    public abstract void click(RPlayer pl);

    public ItemStack getIcon() {
        return ItemBuilder.build(getMaterial(), getName(), getAmount(), getDescription());
    }

    public void setIn(RInventory in) {
        this.in = in;
    }

    private Button getBase() {
        return this;
    }

    private void update() {
        in.setButton(this, new Button() {
            @Override
            public String getName() {
                return newName;
            }

            @Override
            public Material getMaterial() {
                return newMaterial;
            }

            @Override
            public int getAmount() {
                return newAmount;
            }

            @Override
            public List<String> getDescription() {
                return newDesc;
            }

            @Override
            public int getLocation() {
                return newLoc;
            }

            @Override
            public boolean closeOnClick() {
                return newCloseOnClick;
            }

            @Override
            public void click(RPlayer pl) {
                getBase().click(pl);
            }
        });
    }

    public void setName(String name) {
        this.newName = name;
        update();
    }

    public void setMaterial(Material mat) {
        this.newMaterial = mat;
        update();
    }

    public void setAmount(int amount) {
        this.newAmount = amount;
        update();
    }

    public void setDescription(List<String> desc) {
        this.newDesc = desc;
        update();
    }

    public void setLocation(int loc) {
        this.newLoc = loc;
        update();
    }

    public void setCloseOnClick(boolean bool) {
        this.newCloseOnClick = bool;
        update();
    }

}
