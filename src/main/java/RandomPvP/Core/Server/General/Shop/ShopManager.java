package RandomPvP.Core.Server.General.Shop;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ShopManager {

    private static ShopManager clazz;

    private ShopManager() {} //singleton

    /**
     * Get all of the items for a game
     *
     * @param gamename
     * @return Collection of ShopItems
     */
    public Collection<ShopItem> getItemsForGame(final String gamename) {
        Future<Collection<ShopItem>> task = Executors.newCachedThreadPool().submit(new Callable<Collection<ShopItem>>() {
            @Override
            public Collection<ShopItem> call() throws Exception {
                Collection<ShopItem> items = new ArrayList<>();
                {
                    ResultSet res = MySQL.getConnection().prepareStatement("SELECT `name` FROM `items_"+gamename+"`").executeQuery();
                    while (res.next()) {
                        items.add(getItem(res.getString("name"), gamename));
                    }
                }
                return items;
            }
        });
        try {
            return task.get();
        } catch (InterruptedException | ExecutionException ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

    /**
     * Get an item from a game
     *
     * @param name
     * @param gamename
     * @return ShopItem
     */
    public ShopItem getItem(final String name, final String gamename) {
        Future<ShopItem> task = Executors.newCachedThreadPool().submit(new Callable<ShopItem>() {
            @Override
            public ShopItem call() throws Exception {
                PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `items_"+gamename+"` WHERE `name`=?");
                {
                    stmt.setString(1, name);
                }
                ResultSet res = stmt.executeQuery();
                if (res.next()) {
                    final String name = res.getString("name");
                    final String displayname = res.getString("displayname");
                    final List<String> desc = Arrays.asList(res.getString("description").split("\\|"));
                    final Material mat = Material.valueOf(res.getString("material"));
                    final boolean ffd = res.getBoolean("ffd");
                    final int price = res.getInt("price");

                    return new ShopItem() {
                        @Override
                        public String getName() {
                            return name;
                        }

                        @Override
                        public String getDisplayName() {
                            return displayname;
                        }

                        @Override
                        public List<String> getDescription() {
                            return desc;
                        }

                        @Override
                        public Material getMaterial() {
                            return mat;
                        }

                        @Override
                        public boolean freeForDonors() {
                            return ffd;
                        }

                        @Override
                        public int getPrice() {
                            return price;
                        }
                    };
                }

                return null;
            }
        });

        try {
            return task.get();
        } catch (InterruptedException | ExecutionException ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

    /**
     *
     *
     * @param pl
     * @param item
     * @param gamename
     * @return
     */
    public boolean ownsItem(final RPlayer pl, final ShopItem item, final String gamename) {
        try {
            Future<Boolean> task = Executors.newCachedThreadPool().submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    if(item.freeForDonors() && pl.has(Rank.PREMIUM)) {
                        return true;
                    }

                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `owns` FROM `items_"+gamename+"` WHERE `name`=?");
                    {
                        stmt.setString(1, item.getName());
                    }
                    ResultSet res = stmt.executeQuery();
                    if(res.next()) {
                        for(int rpid : StringUtil.stringToIntArray(res.getString("owns").split("\\|"))) {
                            if(pl.getRPID() == rpid) {
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
            return task.get();
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return false;
        }
    }

    public void purchaseItem(final RPlayer pl, final String gamename, final ShopItem item) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement get = MySQL.getConnection().prepareStatement("SELECT `owns` FROM `items_"+gamename+"` WHERE `name`=?");
                    {
                        get.setString(1, item.getName());
                    }
                    ResultSet res = get.executeQuery();
                    if(res.next()) {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `items_"+gamename+"` SET `owns`=? WHERE `name`=?");
                        {
                            stmt.setString(1, res.getString("owns")+"|"+pl.getRPID());
                            stmt.setString(2, item.getName());
                        }
                        stmt.executeUpdate();
                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }


    /**
     * Adds an item if it doesn't already exist
     * @param item
     * @param gamename
     */
    public void addItem(final ShopItem item, final String gamename) {
        createItNotExists(gamename);
        new Thread() {
            public void run() {
                try {
                    PreparedStatement check = MySQL.getConnection().prepareStatement("SELECT `name` FROM `items_"+gamename+"` WHERE `name`=?");
                    {
                        check.setString(1, item.getName());
                    }
                    ResultSet res = check.executeQuery();
                    if(!res.next()) {
                        PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `items_"+gamename+"` VALUES (?,?,?,?,?,?,?)");
                        {
                            insert.setString(1, item.getName());
                            insert.setString(2, item.getDisplayName());
                            insert.setString(3, item.getDescription().toString().replace("[", "").replace("]", "").replace(",", "|")); //we split the data when getting
                            insert.setString(4, item.getMaterial().toString());
                            insert.setInt(5, item.getPrice());
                            insert.setBoolean(6, item.freeForDonors());
                            insert.setString(7, ""); //list of owners... we leave it blank because no one owns it yet
                        }
                        insert.executeUpdate();
                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void removeItem(final ShopItem item, final String gamename) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement check = MySQL.getConnection().prepareStatement("SELECT `name` FROM `items_"+gamename+"` WHERE `name`=?");
                    {
                        check.setString(1, item.getName());
                    }
                    ResultSet res = check.executeQuery();
                    if(res.next()) {
                        PreparedStatement delete = MySQL.getConnection().prepareStatement("DELETE FROM `items_"+gamename+"` WHERE `name`=?");
                        {
                            delete.setString(1, item.getName());
                        }
                        delete.executeUpdate();
                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void createItNotExists(final String gamename) {
        new Thread() {
            public void run() {
                try {
                    MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `items_" + gamename + "` (" +
                            "`name` VARCHAR(255) NOT NULL," +
                            "`displayname` VARCHAR(255) NOT NULL," +
                            "`description` VARCHAR(255) NOT NULL," +
                            "`material` VARCHAR(255) NOT NULL," +
                            "`price` INT(255) NOT NULL," +
                            "`ffd` TINYINT(8) NOT NULL," +
                            "`owns` VARCHAR(255) NOT NULL," +
                            "PRIMARY KEY (`name`)" +
                            ")").executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void loadGlobalItems() {
        addItem(new ShopItem() {
            @Override
            public String getName() {
                return "Prime";
            }

            @Override
            public String getDisplayName() {
                return ChatColor.GOLD.toString() + ChatColor.BOLD + "PRIME Rank";
            }

            @Override
            public List<String> getDescription() {
                return Arrays.asList(ChatColor.GRAY + "A credit purchasable rank? I think so!", "",
                        ChatColor.RED + "Perks:", ChatColor.WHITE + "- " + ChatColor.GRAY + "2x Credits", ChatColor.WHITE + "- " + ChatColor.GRAY + "Fly in supported servers");
            }

            @Override
            public Material getMaterial() {
                return Material.GOLD_HELMET;
            }

            @Override
            public boolean freeForDonors() {
                return true;
            }

            @Override
            public int getPrice() {
                return 2000;
            }
        }, "global");
    }

    public static ShopManager getInstance() {
        if(clazz == null) clazz = new ShopManager();
        return clazz;
    }

}
