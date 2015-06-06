package RandomPvP.Core.Server.General.Shop;

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

    public String getDisplayName();

    public List<String> getDescription();

    public Material getMaterial();

    public boolean freeForDonors();

    public int getPrice();

}
