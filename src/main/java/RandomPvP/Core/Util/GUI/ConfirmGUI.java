package RandomPvP.Core.Util.GUI;

import RandomPvP.Core.Player.Inventory.Button;
import RandomPvP.Core.Player.Inventory.InventoryType;
import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
public abstract class ConfirmGUI {

    public abstract void accept(RPlayer pl);

    public ConfirmGUI(RPlayer pl, ItemStack icon) {
        RInventory inv = new RInventory(45, InventoryType.MENU, "Confirm");
        {
            inv.setItem(22, icon);

            inv.fill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7));

            for(int i=0; i < 45; i++) {
                final int i2 = i;
                if(i < 2 || (i > 8 && i < 11) || (i > 17 && i < 20) || (i > 26 && i < 29) || (i > 35 && i < 38)) {
                    inv.addButton(new Button() {
                        @Override
                        public String getName() {
                            return ChatColor.GREEN.toString() + ChatColor.BOLD + "ACCEPT";
                        }

                        @Override
                        public Material getMaterial() {
                            return Material.EMERALD_BLOCK;
                        }

                        @Override
                        public int getAmount() {
                            return 1;
                        }

                        @Override
                        public List<String> getDescription() {
                            return Arrays.asList(ChatColor.GRAY + "Click to confirm.");
                        }

                        @Override
                        public int getLocation() {
                            return i2;
                        }

                        @Override
                        public boolean closeOnClick() {
                            return true;
                        }

                        @Override
                        public void click(RPlayer pl) {
                            accept(pl);
                        }
                    });
                } else if(i > 6 && i < 9 || i > 15 && i < 18 || i > 24 && i < 27 || i > 33 && i < 36 || i > 42) {
                    inv.addButton(new Button() {
                        @Override
                        public String getName() {
                            return ChatColor.RED.toString() + ChatColor.BOLD + "CANCEL";
                        }

                        @Override
                        public Material getMaterial() {
                            return Material.REDSTONE_BLOCK;
                        }

                        @Override
                        public int getAmount() {
                            return 1;
                        }

                        @Override
                        public List<String> getDescription() {
                            return Arrays.asList(ChatColor.GRAY + "Click to cancel and nothing else will happen.");
                        }

                        @Override
                        public int getLocation() {
                            return i2;
                        }

                        @Override
                        public boolean closeOnClick() {
                            return true;
                        }

                        @Override
                        public void click(RPlayer pl) {
                            pl.message(MsgType.INFO, "Successfully cancelled the confirmation.");
                        }
                    });
                }
            }
        }
        pl.openInventory(inv);
    }

}
