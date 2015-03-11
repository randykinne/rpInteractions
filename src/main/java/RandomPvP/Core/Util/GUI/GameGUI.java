package RandomPvP.Core.Util.GUI;

import RandomPvP.Core.Game.GameState.GameState;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.ItemBuilder;
import RandomPvP.Core.Util.MotdData.MotdFetcher;
import RandomPvP.Core.Util.NetworkUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class GameGUI implements Listener {

    private Inventory inv;
    private String name;
    private String gametype;
    private String sid;
    private ItemStack icon;
    private Inventory mainMenu;

    public GameGUI(String name, String gametype, String sid, ItemStack icon, Inventory mainMenu) {
        this.name = name;
        this.gametype = gametype;
        this.sid = sid;
        this.icon = icon;
        this.mainMenu = mainMenu;

        Bukkit.getServer().getPluginManager().registerEvents(this, RPICore.getInstance());
    }

    public ChatColor getColorForState(GameState s) {
        switch(s) {
            case NONE: return ChatColor.DARK_GRAY;
            case LOBBY: return ChatColor.GREEN;
            case STARTING: return ChatColor.YELLOW;
            case LOADING: return ChatColor.RED;
            case WARMUP: return ChatColor.RED;
            case GRACE: return ChatColor.GREEN;
            case BATTLE: return ChatColor.GREEN;
            case BUILD: return ChatColor.GREEN;
            case RUNNING: return ChatColor.GREEN;
            case INGAME: return ChatColor.GREEN;
            case DEATHMATCH: return ChatColor.YELLOW;
            case ENDED: return ChatColor.DARK_GRAY;
        }
        return ChatColor.DARK_GRAY;
    }

    public Material getIconForState(GameState s) {
        switch(s) {
            case NONE: return Material.BEDROCK;
            case LOBBY: return Material.EMERALD_BLOCK;
            case STARTING: return Material.REDSTONE_BLOCK;
            case LOADING: return Material.REDSTONE_BLOCK;
            case WARMUP: return Material.REDSTONE_BLOCK;
            case GRACE: return Material.REDSTONE_BLOCK;
            case BATTLE: return Material.REDSTONE_BLOCK;
            case BUILD: return Material.REDSTONE_BLOCK;
            case RUNNING: return Material.GOLD_BLOCK;
            case INGAME: return Material.GOLD_BLOCK;
            case DEATHMATCH: return Material.REDSTONE_BLOCK;
            case ENDED: return Material.BEDROCK;
        }
        return Material.BEDROCK;
    }

    public List<String> getLore(MotdFetcher fetcher) throws SQLException {
        List<String> lore = new ArrayList<>();
        {
            if(fetcher.getMotd() != null) {
                String[] motd = NetworkUtil.convertServerData(fetcher);
                int maxPlayers;
                int onlinePlayers;
                {
                    maxPlayers = fetcher.getMaxPlayers();
                    onlinePlayers = fetcher.getOnlinePlayers();
                }
                {
                    lore.add(ChatColor.RED + "Map: " + ChatColor.GRAY + motd[0].replace(motd[0].charAt(0)+"", "")); //fixing random characters at the beginning of map names
                    lore.add(ChatColor.RED + "Game State: " + ChatColor.GRAY + GameState.valueOf(motd[1].toUpperCase()).getName());
                    lore.add(ChatColor.RED + "Game Mode: " + ChatColor.GRAY + motd[2]);
                    if(!motd[3].equals("PLAYER")) {
                        lore.add(ChatColor.RED + "Rank Needed: " + Rank.valueOf(motd[3]).getFormattedName());
                    }
                    lore.add("");
                    lore.add(ChatColor.GRAY.toString() + onlinePlayers + ChatColor.DARK_GRAY + "/" + ChatColor.GRAY + maxPlayers);
                    if(maxPlayers - onlinePlayers == 0) {
                        lore.add(ChatColor.RED + "The server is full!");
                    }
                }
            } else {
                lore.add(ChatColor.GRAY + "Server is offline or restarting");
            }
        }
        return lore;
    }

    public GameState getState(String supposed) {
        GameState state;
        {
            try {
                state = GameState.valueOf(supposed.toUpperCase());
            } catch (Exception ex) {
                state = GameState.NONE;
            }
        }
        return state;
    }

    public void build() {
        inv = Bukkit.getServer().createInventory(null, 54, name);

        inv.setItem(0, ItemBuilder.build(Material.ARROW, ChatColor.DARK_GRAY + "< Go Back <", 1));
        inv.setItem(4, icon);

        new BukkitRunnable() {
            public void run() {
                for (int i = 1; i < 37; i++) {
                    if (PlayerManager.getInstance().getOnlinePlayers().size() > 0) {
                        try {
                            ResultSet res = NetworkUtil.getServerAddress(gametype, i);
                            if (res.next()) {
                                MotdFetcher fetcher = new MotdFetcher(res.getString("ip"), res.getInt("port"));
                                fetcher.fetch();
                                List<String> lore = getLore(fetcher);
                                GameState state;
                                {
                                    if (lore.contains(ChatColor.RED + "The server is full!")) {
                                        state = GameState.STARTING;
                                    } else if (fetcher.getMotd() != null) {
                                        state = getState(NetworkUtil.convertServerData(fetcher)[1]);
                                    } else {
                                        state = GameState.NONE;
                                    }
                                }

                                inv.setItem(i + 8, ItemBuilder.build(getIconForState(state), getColorForState(state).toString() + ChatColor.BOLD + "SERVER " + i, 1, lore));
                            } else {
                                inv.setItem(i + 8, ItemBuilder.build(Material.BEDROCK, ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "SERVER ?", 1, Arrays.asList(ChatColor.GRAY + "Nulled server")));
                            }
                            res.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            inv.setItem(i + 8, ItemBuilder.build(Material.BEDROCK, ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "SERVER ?", 1, Arrays.asList(ChatColor.GRAY + "Nulled server")));
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(RPICore.getInstance(), 70L, 70L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getInventory().equals(getInventory())) {
            if(e.getCurrentItem() != null) {
                e.setCancelled(true);
                if(e.getCurrentItem().hasItemMeta()) {
                    if(e.getCurrentItem().getItemMeta().hasDisplayName()) {
                        ItemStack item = e.getCurrentItem();
                        RPlayer pl = PlayerManager.getInstance().getPlayer((Player) e.getWhoClicked());
                        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                        if(name.equals("< Go Back <")) {
                            pl.getPlayer().closeInventory();
                            pl.getPlayer().openInventory(mainMenu);
                        } else if(name.startsWith("SERVER")) {
                            if(item.getType() == Material.EMERALD_BLOCK || item.getType() == Material.GOLD_BLOCK) {
                                String s = sid.toUpperCase() + name.replace("SERVER ", "");
                                pl.getPlayer().closeInventory();
                                pl.send(s);
                            }
                        }
                    }
                }
            }
        }
    }

    public Inventory getInventory() {
        return inv;
    }

}
