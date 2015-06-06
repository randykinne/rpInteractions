package RandomPvP.Core.Util.GUI;

import RandomPvP.Core.Player.Inventory.Button;
import RandomPvP.Core.Player.Inventory.InventoryType;
import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Server.General.Shop.ShopItem;
import RandomPvP.Core.Server.General.Shop.ShopManager;
import RandomPvP.Core.Util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
public class ShopGUI {

    private String gamename;
    private String name;
    private ItemStack icon;

    public ShopGUI(String name, String gamename, ItemStack icon) {
        this.gamename = gamename;
        this.name = name;
        this.icon = icon;
    }

    public void open(final RPlayer pl) {
        ShopManager.getInstance().createItNotExists(gamename);
        RInventory inv = new RInventory(54, InventoryType.SHOP, name);
        {
            inv.setItem(4, icon);

            List<ShopItem> list = new ArrayList<>();
            {
                list.addAll(ShopManager.getInstance().getItemsForGame(gamename));
            }

            for(int i=1; i < 37; i++) {
                try {
                    final ShopItem item = list.get(i-1);
                    final int i2 = i;
                    inv.addButton(new Button() {
                        @Override
                        public String getName() {
                            return item.getDisplayName();
                        }

                        @Override
                        public Material getMaterial() {
                            return item.getMaterial();
                        }

                        @Override
                        public int getAmount() {
                            return 1;
                        }

                        @Override
                        public List<String> getDescription() {
                            List<String> desc = new ArrayList<>();
                            {
                                desc.addAll(item.getDescription());
                                desc.add("");
                                desc.add(ChatColor.GRAY + "Price: " + ChatColor.RED + (ShopManager.getInstance().ownsItem(pl, item, gamename) ? "Owned" :
                                        (item.getPrice() != 0 ? item.getPrice() + " Credits" : "Free")));
                            }
                            return desc;
                        }

                        @Override
                        public int getLocation() {
                            return i2 + 8;
                        }

                        @Override
                        public boolean closeOnClick() {
                            return false;
                        }

                        @Override
                        public void click(RPlayer pl) {
                            if (!ShopManager.getInstance().ownsItem(pl, item, gamename)) {
                                if (pl.getCredits() >= item.getPrice()) {
                                    new ConfirmGUI(pl, ItemBuilder.build(Material.GLOWSTONE_DUST, ChatColor.YELLOW.toString() + ChatColor.BOLD + "SHOP PURCHASE: " + item.getDisplayName(), 1, item.getDescription())) {
                                        @Override
                                        public void accept(RPlayer pl) {
                                            ShopManager.getInstance().purchaseItem(pl, gamename, item);
                                            pl.removeCredits(item.getPrice());
                                            pl.message(MsgType.INFO, "Successfully bought " + item.getName() + ".");
                                        }
                                    };
                                } else {
                                    pl.message(MsgType.ERROR, "You cannot afford this item.");
                                }
                            } else {
                                pl.message(MsgType.ERROR, "You already own this item.");
                            }
                        }
                    });
                } catch (Exception ex) {
                    inv.setItem(i + 8, ItemBuilder.build(Material.BEDROCK, ChatColor.RED.toString() + "Unknown Item", 1, Arrays.asList(ChatColor.GRAY + "Unknown item")));
                }
            }
        }
        pl.openInventory(inv);
    }
}
