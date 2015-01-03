package RandomPvP.Core.Commands.Mod;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.ServerToggles;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public class ModPanelCmd implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) e.getWhoClicked());
        Bukkit.broadcastMessage("0");
        if (e.getClickedInventory().getTitle().equalsIgnoreCase("§5Moderator Panel")) {
            if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lRank Whitelist") || e.getCurrentItem().getType() == Material.NAME_TAG) {
                Bukkit.broadcastMessage("1");
                openRankWhitelistInv(pl);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lToggle Chat")) {
                Bukkit.broadcastMessage("2");
                Bukkit.getServer().dispatchCommand(pl.getPlayer(), "silencechat");
                pl.getPlayer().closeInventory();
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6§lToggle Build/Edit Mode")) {
                Bukkit.broadcastMessage("3");
                ServerToggles.setEditMode(!ServerToggles.isEditMode());
                pl.getPlayer().closeInventory();
                Bukkit.broadcastMessage("§5§l>> " + pl.getRankedName(false) + " §btoggled BUILD mode to" + String.valueOf(ServerToggles.isEditMode()) + ".");
            }

            e.setCancelled(true);
        } else if (e.getClickedInventory().getTitle().equalsIgnoreCase("§5Mod Panel §8> §6Rank")) {
            if (e.getCurrentItem().getType() == Material.ARROW) {
                openModPanel(pl);
            } else if (e.getCurrentItem().getType() == Material.WOOL) {
                ServerToggles.setRankRequired(Rank.valueOf(e.getCurrentItem().getItemMeta().getLore().get(0).replace("§0", "")));
                Bukkit.broadcastMessage("§5§l>> " + pl.getRankedName(false) + " §bset the minimum rank to §l" + Rank.valueOf(e.getCurrentItem().getItemMeta().getLore().get(0).replace("§0", "")).getName() + "§b.");
            }
            e.setCancelled(true);
        }
    }

    public static void openModPanel(RPlayer pl) {
        Player player = pl.getPlayer();
        pl.message("§8§l>> §5Opening Mod Panel...");

        Inventory inv = Bukkit.createInventory(null, 54, "§5Moderator Panel");

        for (int i = 0; i<inv.getSize(); i++) {
            {
                ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
        }
        {
            ItemStack item = new ItemStack(Material.COMMAND);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6§lServer Settings");
            meta.setLore(Arrays.asList("§7Various settings for the server."));
            item.setItemMeta(meta);
            inv.setItem(16, item);
        }
        {
            ItemStack item = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6§lRank Whitelist");
            meta.setLore(Arrays.asList("§7Shows current rank whitelist."));
            item.setItemMeta(meta);
            inv.setItem(25, item);
        }
        {
            ItemStack item = new ItemStack(Material.BLAZE_POWDER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6Toggle Chat");
            meta.setLore(Arrays.asList("§7Toggles whether non-staff can chat"));
            item.setItemMeta(meta);
            inv.setItem(34, item);
        }
        {
            ItemStack item = new ItemStack(Material.WORKBENCH);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6§lToggle Build/Edit Mode");
            meta.setLore(Arrays.asList("§7Toggles build/edit mode"));
            item.setItemMeta(meta);
            inv.setItem(43, item);
        }

        player.openInventory(inv);

    }

    private static void openRankWhitelistInv(RPlayer pl) {
        Player player = pl.getPlayer();

        Inventory inv = Bukkit.createInventory(null, 45, "§5Mod Panel §8> §6Rank");

        for (int i = 0; i<inv.getSize(); i++) {
            {
                ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(" ");
                item.setItemMeta(meta);
                inv.setItem(i, item);
            }
        }
        {
            ItemStack item = new ItemStack(Material.ARROW);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§8< Go Back <");
            item.setItemMeta(meta);
            inv.setItem(0, item);
        }
        {
            ItemStack item = new ItemStack(Material.ANVIL);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§6§lCurrent Rank Whitelist");
            meta.setLore(Arrays.asList("§l" + ServerToggles.getRankRequired().getName()));
            item.setItemMeta(meta);
            inv.setItem(3, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 8);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.PLAYER.getName());
            meta.setLore(Arrays.asList("§0PLAYER"));
            item.setItemMeta(meta);
            inv.setItem(19, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 3);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.PREMIUM.getName());
            meta.setLore(Arrays.asList("§0PREMIUM"));
            item.setItemMeta(meta);
            inv.setItem(20, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 4);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.VIP.getName());
            meta.setLore(Arrays.asList("§0VIP"));
            item.setItemMeta(meta);
            inv.setItem(21, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 13);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.BUILDER.getName());
            meta.setLore(Arrays.asList("§0BUILDER"));
            item.setItemMeta(meta);
            inv.setItem(22, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 10);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.MOD.getName());
            meta.setLore(Arrays.asList("§0MOD"));
            item.setItemMeta(meta);
            inv.setItem(23, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 6);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.ADMIN.getName());
            meta.setLore(Arrays.asList("§0ADMIN"));
            item.setItemMeta(meta);
            inv.setItem(24, item);
        }
        {
            ItemStack item = new ItemStack(Material.WOOL, 1, (short) 14);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7Set required to §l" + Rank.OWNER.getName());
            meta.setLore(Arrays.asList("§0OWNER"));
            item.setItemMeta(meta);
            inv.setItem(25, item);
        }

        player.openInventory(inv);
    }

    @Command(aliases = "modpanel", desc = "Opens mod panel", usage = "-")
    public static void modPanel(final CommandContext args, CommandSender sender) throws CommandException {
        if (sender instanceof Player) {
             RPlayer pl = RPlayerManager.getInstance().getPlayer((Player) sender);
            if (pl.isStaff()) {
                openModPanel(pl);
            } else {
                throw new CommandException("You must be Mod to use this command.");
            }
        } else {
            throw new CommandException("You must be a mod/player to use this command!");
        }
    }
}
