package RandomPvP.Core.Util.Shop.GUI.TypeGUI;

import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.Shop.GUI.MainGUI;
import RandomPvP.Core.Util.Shop.ShopItem;
import RandomPvP.Core.Util.Shop.ShopManager;
import RandomPvP.Core.Util.ItemBuilder;
import RandomPvP.Core.Util.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class GizmoGUI implements Listener {

    public static void openGUI(RPlayer pl) {
        ArrayList<ShopItem> items = ShopManager.getInstance().getItemsByType(ShopManager.ItemType.GIZMO);
        Inventory inv = Bukkit.createInventory(pl.getPlayer(), NumberUtil.getSlotsNeeded(items.size()) + 18, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Shop - Gizmo Items");
        {
            for(int i=0; i < inv.getSize(); i++) {
                inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
            }
            for(int i=0; i < NumberUtil.getSlotsNeeded(items.size())-2; i++) {
                try {
                    ShopItem item = items.get(i);
                    inv.setItem(i+10, ItemBuilder.build(item.getIcon(), item.getColor() + item.getName(), 1, item.getDiscription()));
                } catch (Exception ex) {
                    inv.setItem(i+10, ItemBuilder.build(Material.BEDROCK, ChatColor.YELLOW + "Unknown Item", 1, Arrays.asList(ChatColor.GRAY + "There is no item under this count.")));
                }
            }
            inv.setItem(0, ItemBuilder.build(Material.ARROW, ChatColor.DARK_GRAY + "< Go Back <", 1));
        }
        pl.getPlayer().openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().getName().equals(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Shop - Gizmo Items")) {
            if(e.getCurrentItem() != null) {
                e.setCancelled(true);
                if(e.getCurrentItem().hasItemMeta()) {
                    if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        for(ShopItem item : ShopManager.getInstance().getItemsByType(ShopManager.ItemType.GIZMO)) {
                            if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equals(item.getName())) {
                                RPlayer pl = PlayerManager.getInstance().getPlayer((Player) e.getWhoClicked());
                                if(item.strictRank()) {
                                    if (pl.getRank() == item.getRankNeeded()) {
                                        if (pl.getCredits() >= item.getPrice()) {
                                            item.purcahse(pl);
                                        } else {
                                            pl.message(MsgType.ERROR, "You don't have enough credits to purchase this item!");
                                        }
                                    } else {
                                        pl.message(MsgType.ERROR, "You need rank " + item.getRankNeeded().getFormattedName() + ChatColor.GRAY + ChatColor.ITALIC + " to purchase this item.");
                                    }
                                } else {
                                    if (pl.has(item.getRankNeeded())) {
                                        if (pl.getCredits() >= item.getPrice()) {
                                            item.purcahse(pl);
                                        } else {
                                            pl.message(MsgType.ERROR, "You don't have enough credits to purchase this item!");
                                        }
                                    } else {
                                        pl.message(MsgType.ERROR, "You need rank " + item.getRankNeeded().getFormattedName() + ChatColor.GRAY + ChatColor.ITALIC + " to purchase this item.");
                                    }
                                }
                                return;
                            }
                        }
                        e.getWhoClicked().closeInventory();
                        MainGUI.openGUI(PlayerManager.getInstance().getPlayer((Player) e.getWhoClicked()));
                    }
                }
            }
        }
    }

}
