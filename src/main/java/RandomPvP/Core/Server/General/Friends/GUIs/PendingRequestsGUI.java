package RandomPvP.Core.Server.General.Friends.GUIs;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Server.General.Friends.FriendBase;
import RandomPvP.Core.Util.GUI.AnvilGUI;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class PendingRequestsGUI implements Listener {

    private Inventory inv;

    public PendingRequestsGUI(Player p) {
        Bukkit.getServer().getPluginManager().registerEvents(this, RPICore.getInstance());
        RPlayer pl = PlayerManager.getInstance().getPlayer(p);

        FriendBase base = new FriendBase();
        inv = Bukkit.getServer().createInventory(p, 54, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC + "Friends");
        {
            inv.setItem(3, ItemBuilder.build(Material.SKULL_ITEM, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PENDING REQUESTS",
                    base.getWaitingRequestsArray(new OfflineRPlayer(pl.getName())).length));
            inv.setItem(4, ItemBuilder.build(Material.ANVIL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "ADD FRIEND", 1));
            inv.setItem(5, ItemBuilder.build(Material.SKULL_ITEM, ChatColor.YELLOW.toString() + ChatColor.BOLD + "CURRENT FRIENDS", 1));

            for (int i = 0; i < 46; i++) {
                OfflineRPlayer request;
                try {
                    request = new OfflineRPlayer(base.getWaitingRequestsArray(new OfflineRPlayer(p.getName()))[i]);
                } catch (Exception ignored) {
                    return;
                }
                inv.setItem(i + 9, ItemBuilder.build(Material.SKULL_ITEM, request.getRankedName(false), 1, Arrays.asList(
                        ChatColor.GREEN + "Click To Accept"
                )));
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().equals(getInventory())) {
            if(e.getCurrentItem() != null) {
                e.setCancelled(true);
                if(e.getCurrentItem().hasItemMeta()) {
                    if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        final RPlayer pl = PlayerManager.getInstance().getPlayer((Player) e.getWhoClicked());
                        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                        if(name.equals("CURRENT FRIENDS")) {
                            e.getWhoClicked().closeInventory();
                            e.getWhoClicked().openInventory(new FriendsGUI(pl.getPlayer()).getInventory());
                        } else if(name.equals("ADD FRIEND")) {
                            AnvilGUI gui = new AnvilGUI((Player) e.getWhoClicked(), new AnvilGUI.AnvilClickEventHandler() {
                                @Override
                                public void onAnvilClick(AnvilGUI.AnvilClickEvent e) {
                                    handleAddFriend(e, pl);
                                }
                            });
                            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, ItemBuilder.build(Material.NAME_TAG, "Player Name"));
                            gui.open();
                        } else if(e.getCurrentItem().getType() == Material.SKULL_ITEM) {
                            handleAcceptRequest(e);
                        }
                    }
                }
            }
        }
    }

    public void handleAcceptRequest(InventoryClickEvent e) {
        FriendBase base = new FriendBase();
        ItemStack skull = e.getCurrentItem();
        String requestedName = ChatColor.stripColor(skull.getItemMeta().getDisplayName());
        {
            base.removeWaitingRequest(new OfflineRPlayer(e.getWhoClicked().getName()), requestedName);
            base.addFriend(new OfflineRPlayer(e.getWhoClicked().getName()), requestedName);
            base.addFriend(new OfflineRPlayer(requestedName), e.getWhoClicked().getName());
        }
        e.getWhoClicked().closeInventory();
        PlayerManager.getInstance().getPlayer(e.getWhoClicked().getName()).message(MsgType.INFO, "Successfully added "
                + new OfflineRPlayer(requestedName).getRankedName(false) + ChatColor.GRAY + " as a friend.");
    }

    public void handleAddFriend(AnvilGUI.AnvilClickEvent e, RPlayer pl) {
        if(e.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
            try {
                OfflineRPlayer searchPlayer = new OfflineRPlayer(e.getName());
                {
                    if (searchPlayer.getName().equals(pl.getName())) {
                        pl.message(MsgType.ERROR, "You cannot friend yourself.");
                        return;
                    }
                }
                {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `rpid` FROM `accounts` WHERE `uuid` = ?");
                    {
                        stmt.setString(1, searchPlayer.getUUID().toString());
                    }
                    ResultSet res = stmt.executeQuery();
                    while (res.next()) {
                        FriendBase base = new FriendBase();
                        if(base.getWaitingRequests(searchPlayer).contains(pl.getRPID()+"")) {
                            pl.message(MsgType.ERROR, "You have already sent this person a request.");
                            return;
                        } else if(isInIntArray(base.getWaitingRequestsArray(new OfflineRPlayer(pl.getName())), searchPlayer.getRPID())) {
                            pl.message(MsgType.ERROR, "The player you are trying to friend has already sent you a request.");
                            return;
                        } else {
                            base.addWaitingRequest(searchPlayer, pl.getName());
                            pl.message(MsgType.INFO, "Successfully sent a friend request to " + searchPlayer.getRankedName(false) + ChatColor.GRAY + ".");
                            return;
                        }
                    }
                    pl.message(MsgType.ERROR, searchPlayer.getRankedName(false) + ChatColor.GRAY + " has never played on the network.");
                }
            } catch (Exception ex) {
                e.setWillClose(true);
                e.setWillDestroy(true);
                pl.message(MsgType.ERROR, "There was an error. Please try again later.");
            }
        }
    }

    public void close(PreparedStatement stmt, ResultSet res, AnvilGUI.AnvilClickEvent e) throws SQLException {
        stmt.close();
        res.close();
        e.setWillClose(true);
        e.setWillDestroy(true);
    }

    public Inventory getInventory() {
        return inv;
    }

    public boolean isInIntArray(int[] array, int i) {
        for(int i2 : array) {
            if(i2 == i) {
                return true;
            }
        }
        return false;
    }

}
