package RandomPvP.Core.Player.Inventory;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public enum InventoryType {

    SHOP("SHOP"), SPECTATE("SPECTATE"), MENU("MENU"), OTHER("");

    private String name;

    InventoryType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
