package RandomPvP.Core.Util.Shop;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public interface ShopItem {

    public String getName();

    public ChatColor getColor();

    public List<String> getDiscription();

    public ShopManager.ItemType getType();

    public Rank getRankNeeded();

    public boolean strictRank();

    public int getPrice();

    public Material getIcon();

    public void purcahse(RPlayer pl);

}
