package RandomPvP.Core.Util.Shop;

import RandomPvP.Core.Util.Shop.GUI.MainGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.CreditGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.GizmoGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.MiscGUI;
import RandomPvP.Core.Util.Shop.GUI.TypeGUI.RankGUI;
import RandomPvP.Core.RPICore;

import java.util.ArrayList;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ShopManager {

    protected static ShopManager clazz;
    private ArrayList<ShopItem> items = new ArrayList<ShopItem>();

    public ShopManager() {
        clazz = this;

        RPICore.getInstance().addListener(new MainGUI());
        RPICore.getInstance().addListener(new CreditGUI());
        RPICore.getInstance().addListener(new GizmoGUI());
        RPICore.getInstance().addListener(new MiscGUI());
        RPICore.getInstance().addListener(new RankGUI());
    }

    public void addItem(ShopItem item) {
        items.add(item);
    }

    public void removeItem(ShopItem item) {
        items.remove(item);
    }

    public ShopItem getItem(String name) {
        for(ShopItem item : getItems()) {
            if(item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public ArrayList<ShopItem> getItems() {
        return items;
    }

    public ArrayList<ShopItem> getItemsByType(ItemType type) {
        ArrayList<ShopItem> itemtype = new ArrayList<ShopItem>();
        for(ShopItem item : getItems()) {
            if(item.getType() == type) {
                itemtype.add(item);
            }
        }
        return itemtype;
    }

    public static ShopManager getInstance() {
        if(clazz == null) {
            clazz = new ShopManager();
        }
        return clazz;
    }
    
    public enum ItemType {
        CREDIT, RANK, GIZMO, MISC
    }

}
