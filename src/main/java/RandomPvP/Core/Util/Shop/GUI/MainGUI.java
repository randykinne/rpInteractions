package RandomPvP.Core.Util.Shop.GUI;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.CreditGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.GizmoGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.MiscGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.RankGUI;
import RandomPvP.Core.Util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class MainGUI implements Listener {

    public static void openGUI(RPlayer pl) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Shop");
        {
            for(int i=0; i < 27; i++) {
                inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
            }
            inv.setItem(11, ItemBuilder.build(Material.CHEST, ChatColor.RED + "Credits Shop", 1));
            inv.setItem(12, ItemBuilder.build(Material.CHEST, ChatColor.RED + "Rank Shop", 1));
            inv.setItem(14, ItemBuilder.build(Material.CHEST, ChatColor.RED + "Gizmo Shop", 1));
            inv.setItem(15, ItemBuilder.build(Material.CHEST, ChatColor.RED + "Miscellaneous Shop", 1));
        }
        pl.getPlayer().openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().getName().equalsIgnoreCase(ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Shop")) {
            if(e.getCurrentItem() != null) {
                e.setCancelled(true);
                if(e.getCurrentItem().hasItemMeta()) {
                    if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        RPlayer pl = PlayerManager.getInstance().getPlayer((Player) e.getWhoClicked());
                        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        if(name.equals("Credits Shop")) {
                            e.getWhoClicked().closeInventory();
                            CreditGUI.openGUI(pl);
                        } else if(name.equals("Rank Shop")) {
                            e.getWhoClicked().closeInventory();
                            RankGUI.openGUI(pl);
                        } else if(name.equals("Gizmo Shop")) {
                            e.getWhoClicked().closeInventory();
                            GizmoGUI.openGUI(pl);
                        } else if(name.equals("Miscellaneous Shop")) {
                            e.getWhoClicked().closeInventory();
                            MiscGUI.openGUI(pl);
                        }
                    }
                }
            }
        }
    }

}
