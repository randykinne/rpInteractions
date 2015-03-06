package RandomPvP.Core.Util.Shop.Items;

import RandomPvP.Core.Player.Credit.Booster;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.Shop.ShopItem;
import RandomPvP.Core.Util.Shop.ShopManager;
import RandomPvP.Core.Util.Color.CC;
import org.bukkit.ChatColor;
import org.bukkit.Material;

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
public class BoosterItem implements ShopItem {

    @Override
    public String getName() {
        return "Credit Booster";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public List<String> getDiscription() {
        return Arrays.asList("", CC.GRY+ "Receive x2 credits for one day when",
                CC.GRY + "purchased.", "", CC.RED + "Price: " + CC.GRY + "250 Credits", "");
    }

    @Override
    public ShopManager.ItemType getType() {
        return ShopManager.ItemType.CREDIT;
    }

    @Override
    public Rank getRankNeeded() {
        return Rank.PLAYER;
    }

    @Override
    public boolean strictRank() {
        return false;
    }

    @Override
    public int getPrice() {
        return 250;
    }

    @Override
    public Material getIcon() {
        return Material.ENDER_CHEST;
    }

    @Override
    public void purcahse(RPlayer pl) {
        if(pl.getBooster() == null) {
            Booster boost = new Booster(pl, 86400000, 2);
            boost.activate();
            pl.setBooster(boost);
        } else {
            pl.message(MsgType.ERROR, "You already have an active booster!");
        }
    }

}
