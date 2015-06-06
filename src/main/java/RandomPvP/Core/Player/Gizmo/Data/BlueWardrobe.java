package RandomPvP.Core.Player.Gizmo.Data;

import RandomPvP.Core.Player.Gizmo.Bases.Wardrobe;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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
public class BlueWardrobe extends Wardrobe {

    @Override
    public String getDisplayName() {
        return ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Blue Wardrobe";
    }

    @Override
    public List<String> getDesc() {
        return Arrays.asList(ChatColor.GRAY + "Dress in some fancy blue attire", ChatColor.GRAY + "for the lake ride.");
    }

    @Override
    public int getPrice() {
        return 75;
    }

    @Override
    public boolean donorOnly() {
        return false;
    }

    @Override
    public ItemStack getHelmet() {
        ItemStack stack = new ItemStack(Material.LEATHER_HELMET, 1);
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            {
                meta.setColor(Color.AQUA);
                meta.setDisplayName(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Blue Helmet");
            }
            stack.setItemMeta(meta);
        }
        return stack;
    }

    @Override
    public ItemStack getChest() {
        ItemStack stack = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            {
                meta.setColor(Color.AQUA);
                meta.setDisplayName(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Blue Jacket");
            }
            stack.setItemMeta(meta);
        }
        return null;
    }

    @Override
    public ItemStack getPants() {
        ItemStack stack = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            {
                meta.setColor(Color.AQUA);
                meta.setDisplayName(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Blue Pants");
            }
            stack.setItemMeta(meta);
        }
        return null;
    }

    @Override
    public ItemStack getBoots() {
        ItemStack stack = new ItemStack(Material.LEATHER_BOOTS, 1);
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            {
                meta.setColor(Color.AQUA);
                meta.setDisplayName(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "Blue Shoes");
            }
            stack.setItemMeta(meta);
        }
        return null;
    }

}
