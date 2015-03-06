package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.ItemBuilder;
import net.minecraft.server.v1_7_R4.MaterialPortal;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sun.plugin.net.protocol.jar.CachedJarURLConnection;

import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RulesCmd extends RCommand {

    public RulesCmd() {
        super("rules");
        setMaximumArgs(0);
        setMaximumArgs(0);
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        openRulesInventory(pl);
    }

    public void openRulesInventory(RPlayer pl) {
        Inventory inv = Bukkit.createInventory(null, 36, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "");
        {
            for(int i=0; i < 36; i++) {
                inv.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
            }
            inv.setItem(10, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Cheating", Arrays.asList(ChatColor.GRAY + "Cheating of any type is not permitted. (Hacks, glitches, etc.)")));
            inv.setItem(11, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Mods", Arrays.asList(ChatColor.GRAY + "The only permitted mods are those listed in the approved mods section.")));
            inv.setItem(12, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Staff", Arrays.asList(ChatColor.GRAY + "Listen to staff. If they tell you not to do something, don’t do it.")));
            inv.setItem(13, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Logging", Arrays.asList(ChatColor.GRAY + "Combat-logging to avoid deaths/loss is not permitted.")));
            inv.setItem(14, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Team Killing", Arrays.asList(ChatColor.GRAY + "Playing against your team is not permitted. (When applicable.)")));
            inv.setItem(15, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Teaming/Trucing",
                    Arrays.asList(ChatColor.GRAY + "Teaming/trucing with others is not permitted as it is unfair to others.", ChatColor.GRAY + "This is only applicable in non team based games.")));
            inv.setItem(16, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Skins",
                    Arrays.asList(ChatColor.GRAY + "Inappropriate skins are not allowed.", ChatColor.GRAY + "A ban will be issued and the appeal will be accepted when changed to something decent.")));
            inv.setItem(19, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Chat", Arrays.asList(ChatColor.GRAY + "“Hackusating,” publicly accusing other players of hacking and/or cheating is not allowed. ")));
            inv.setItem(20, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Evading", Arrays.asList(ChatColor.GRAY + "Evading punishments is not permitted. This includes the use of alternate accounts.")));
            inv.setItem(21, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Content", Arrays.asList(ChatColor.GRAY + "Do not post inappropriate content/links.",
                    ChatColor.GRAY + "Mild swearing is allowed if it is not directed at someone.")));
            inv.setItem(22, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Harassment", Arrays.asList(ChatColor.GRAY + "Use of it in excess can and will lead to a *network ban.")));
            inv.setItem(23, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Spam", Arrays.asList(ChatColor.GRAY + "Spamming otherwise unneeded comments is not permitted.")));
            inv.setItem(24, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Trolling", Arrays.asList(ChatColor.GRAY + "Do not post/chat with the intention of being misleading/confusing to other players.")));
            inv.setItem(25, ItemBuilder.build(Material.PAPER, ChatColor.RED + "Unknown", Arrays.asList(ChatColor.GRAY + "Unknown rule.")));
        }
        pl.getPlayer().openInventory(inv);
    }

}
