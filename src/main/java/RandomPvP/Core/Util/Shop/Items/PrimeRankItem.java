package RandomPvP.Core.Util.Shop.Items;

import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.Color.CC;
import RandomPvP.Core.Util.Shop.ShopItem;
import RandomPvP.Core.Util.Shop.ShopManager;
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
public class PrimeRankItem implements ShopItem {

    @Override
    public String getName() {
        return "PRIME Rank";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
    }

    @Override
    public List<String> getDiscription() {
        return Arrays.asList(CC.GRY + "An in-game purchasable rank? Yep!",
                "",
                CC.RED + "Price: " + CC.GRY + "2000 Credits");
    }

    @Override
    public ShopManager.ItemType getType() {
        return ShopManager.ItemType.RANK;
    }

    @Override
    public Rank getRankNeeded() {
        return Rank.PLAYER;
    }

    @Override
    public boolean strictRank() {
        return true;
    }

    @Override
    public int getPrice() {
        return 2000;
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_HELMET;
    }

    @Override
    public void purcahse(RPlayer pl) {
        pl.message(MsgType.INFO, "Congratulations on the PRIME rank!");
        pl.setRank(Rank.PRIME, true);
    }

}
