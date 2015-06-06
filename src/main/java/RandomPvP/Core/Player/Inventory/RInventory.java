package RandomPvP.Core.Player.Inventory;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RInventory implements Listener {

    private Inventory inv;
    private int size;
    private InventoryType type;
    private String name;
    private HashMap<ItemStack, Integer> setItems = new HashMap<>();
    private List<ItemStack> addedItems = new ArrayList<>();
    private List<Button> buttons = new ArrayList<>();
    private List<Player> looking = new ArrayList<>();

    public RInventory(int size, InventoryType type, String name) {
        this.size = size;
        this.type = type;

        if(!name.contains("§")) name = "§0" + name;

        this.name = name;

        update(true);
        RPICore.getInstance().addListener(this);
    }

    public void update(boolean reset) {
        if (reset) inv = Bukkit.createInventory(null, size, (("§8§l" + type.getName() + " " + name).length() >= 32 ? name : "§8§l" + type.getName() + " " + name));
        {
            if(fillStack != null) {
                fill(fillStack);
            }

            for(ItemStack stack : addedItems) {
                inv.addItem(stack);
            }

            for(ItemStack stack : setItems.keySet()) {
                inv.setItem(setItems.get(stack), stack);
            }

            for(Button b : getButtons()) {
                inv.setItem(b.getLocation(), b.getIcon());
            }

            if(reset) {
                for (Player p : looking) {
                    p.openInventory(inv);
                }
            }
        }
    }

    public void addButton(Button b) {
        if(!buttons.contains(b)) buttons.add(b);
        b.setIn(this);

        update(false);
    }

    public void setButton(Button b1, Button b2) {
        if(!buttons.contains(b1)) return;
        buttons.remove(b1);
        b1.setIn(null);

        addButton(b2);
    }

    public void removeButton(Button b) {
        if(buttons.contains(b)) buttons.remove(b);
        b.setIn(null);

        update(false);
    }

    public void clearButtons() {
        buttons.clear();

        for(Button b : getButtons()) {
            b.setIn(null);
        }

        update(false);
    }

    public void clearItems() {
        setItems.clear();
        addedItems.clear();
        update(false);
    }

    public void clear() {
        buttons.clear();

        for(Button b : getButtons()) {
            b.setIn(null);
        }

        setItems.clear();
        addedItems.clear();

        update(false);
    }

    public Collection<Button> getButtons() {
        return buttons;
    }

    public void setItem(int i, ItemStack s) {
        setItems.put(s, i);
        update(false);
    }

    public void addItem(ItemStack... s) {
        for(ItemStack stack : s) {
            addedItems.add(stack);
        }
        update(false);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        update(true);
    }

    public InventoryType getType() {
        return type;
    }

    public void setType(InventoryType type) {
        this.type = type;
        update(true);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        update(true);
    }

    public ItemStack getItem(int i) {
        return inv.getItem(i);
    }

    public ItemStack[] getContents() {
        return inv.getContents();
    }

    public Inventory getInventory() {
        return inv;
    }

    private ItemStack fillStack = null;

    public void fill(ItemStack stack) {
        fillStack = stack;

        for(int i=0; i < inv.getSize(); i++) {
            inv.setItem(i, stack);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().equals(getInventory())) {
            if(e.getCurrentItem() != null) {
                e.setCancelled(true);
                if(e.getCurrentItem().hasItemMeta()) {
                    if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        for(Button b : getButtons()) {
                            if(b.getName().equals(e.getCurrentItem().getItemMeta().getDisplayName()) && b.getLocation() == e.getSlot()) {
                                b.click(PlayerManager.getInstance().getPlayer((Player) e.getWhoClicked()));
                                if(b.closeOnClick()) {
                                    e.getWhoClicked().closeInventory();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if(e.getInventory().equals(getInventory())) {
            if(!looking.contains((Player) e.getPlayer())) {
                looking.add((Player) e.getPlayer());
            }
        } else if(looking.contains((Player) e.getPlayer())) {
            looking.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(e.getInventory().equals(getInventory())) {
            if(looking.contains((Player) e.getPlayer())) {
                looking.remove((Player) e.getPlayer());
            }
        } else if(looking.contains((Player) e.getPlayer())) {
            looking.remove((Player) e.getPlayer());
        }
    }

}
