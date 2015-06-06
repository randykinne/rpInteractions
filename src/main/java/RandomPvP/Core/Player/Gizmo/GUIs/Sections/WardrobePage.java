package RandomPvP.Core.Player.Gizmo.GUIs.Sections;

import RandomPvP.Core.Player.Gizmo.Bases.Wardrobe;
import RandomPvP.Core.Player.Gizmo.GUIs.MainPage;
import RandomPvP.Core.Player.Gizmo.GizmoManager;
import RandomPvP.Core.Player.Inventory.Button;
import RandomPvP.Core.Player.Inventory.InventoryType;
import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Server.General.Shop.ShopManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class WardrobePage {

    public static void open(RPlayer pl) {
        RInventory inv = new RInventory(54, InventoryType.MENU, "Gizmos - Wardrobe");
        {
            inv.addButton(new Button() {
                @Override
                public String getName() {
                    return ChatColor.DARK_GRAY + "< Go Back <";
                }

                @Override
                public Material getMaterial() {
                    return Material.ARROW;
                }

                @Override
                public int getAmount() {
                    return 1;
                }

                @Override
                public List<String> getDescription() {
                    return Arrays.asList(ChatColor.GRAY + "Go back");
                }

                @Override
                public int getLocation() {
                    return 0;
                }

                @Override
                public boolean closeOnClick() {
                    return false;
                }

                @Override
                public void click(RPlayer pl) {
                    pl.openInventory(MainPage.getInventory());
                }
            });

            int slot = 0;
            for(final Wardrobe wardrobe : GizmoManager.getManager().getWardrobes()) {
                if(slot < 55) {
                    if(true) { //TODO - Check if it isn't within the border
                        final int j = slot;
                        inv.addButton(new Button() {
                            @Override
                            public String getName() {
                                return wardrobe.getDisplayName();
                            }

                            @Override
                            public Material getMaterial() {
                                return Material.LEATHER_CHESTPLATE;
                            }

                            @Override
                            public int getAmount() {
                                return 1;
                            }

                            @Override
                            public List<String> getDescription() {
                                List<String> lore = new ArrayList<String>();
                                {
                                    lore.addAll(wardrobe.getDesc());
                                }
                                return lore;
                            }

                            @Override
                            public int getLocation() {
                                return j;
                            }

                            @Override
                            public boolean closeOnClick() {
                                return false;
                            }

                            @Override
                            public void click(RPlayer pl) {
                                if(ShopManager.getInstance().ownsItem(pl, ShopManager.getInstance().getItem(wardrobe.getDisplayName(), "gizmos"), "gizmos")) {

                                } else {
                                    if(wardrobe.donorOnly()) {

                                    }
                                }
                            }
                        });
                    }

                    slot++;
                } else {
                    break;
                }
            }
        }
        pl.openInventory(inv);
    }

}
