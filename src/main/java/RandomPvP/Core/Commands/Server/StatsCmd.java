package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.Inventory.Button;
import RandomPvP.Core.Player.Inventory.InventoryType;
import RandomPvP.Core.Player.Inventory.RInventory;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Stats.Stats;
import org.bukkit.Bukkit;
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
public class StatsCmd extends RCommand {

    public StatsCmd() {
        super("stats");
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        final Stats stats;
        final String name;
        {
            if (args.length == 0) {
                stats = pl.getStats();
                name = pl.getName();
            } else {
                if(Bukkit.getPlayer(args[0]) != null) {
                    stats = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0])).getStats();
                    name = PlayerManager.getInstance().getPlayer(Bukkit.getPlayer(args[0])).getName();
                } else {
                    stats = new OfflineRPlayer(args[0]).getStats();
                    name = new OfflineRPlayer(args[0]).getName();
                }
            }
        }

        if(stats != null) {
            RInventory inv = new RInventory(27, InventoryType.OTHER, name + "'s Stats");
            {
                inv.fill(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));

                inv.addButton(new Button() {
                    @Override
                    public String getName() {
                        return ChatColor.GREEN + "Kills";
                    }

                    @Override
                    public int getLocation() {
                        return 11;
                    }

                    @Override
                    public boolean closeOnClick() {
                        return false;
                    }

                    @Override
                    public Material getMaterial() {
                        return Material.DIAMOND_SWORD;
                    }

                    @Override
                    public int getAmount() {
                        return 1;
                    }

                    @Override
                    public List<String> getDescription() {
                        return Arrays.asList(ChatColor.GRAY + "Total Kills: " + ChatColor.RED + stats.getKills());
                    }

                    @Override
                    public void click(RPlayer pl) {}
                });

                inv.addButton(new Button() {
                    @Override
                    public String getName() {
                        return ChatColor.YELLOW + "KDR";
                    }

                    @Override
                    public int getLocation() {
                        return 13;
                    }

                    @Override
                    public boolean closeOnClick() {
                        return false;
                    }

                    @Override
                    public Material getMaterial() {
                        return Material.DIAMOND_CHESTPLATE;
                    }

                    @Override
                    public int getAmount() {
                        return 1;
                    }

                    @Override
                    public List<String> getDescription() {
                        return Arrays.asList(ChatColor.GRAY + "KDR: " + ChatColor.RED + stats.getKdr());
                    }

                    @Override
                    public void click(RPlayer pl) {}
                });

                inv.addButton(new Button() {
                    @Override
                    public String getName() {
                        return ChatColor.RED + "Deaths";
                    }

                    @Override
                    public int getLocation() {
                        return 15;
                    }

                    @Override
                    public boolean closeOnClick() {
                        return false;
                    }

                    @Override
                    public Material getMaterial() {
                        return Material.SKULL_ITEM;
                    }

                    @Override
                    public int getAmount() {
                        return 1;
                    }

                    @Override
                    public List<String> getDescription() {
                        return Arrays.asList(ChatColor.GRAY + "Total Deaths: " + ChatColor.RED + stats.getDeaths());
                    }

                    @Override
                    public void click(RPlayer pl) {}
                });
            }
            pl.openInventory(inv);
        } else {
            pl.message(MsgType.ERROR, "Could not find the stats of " + ChatColor.BLUE + name + ChatColor.GRAY + ".");
        }
    }

}
