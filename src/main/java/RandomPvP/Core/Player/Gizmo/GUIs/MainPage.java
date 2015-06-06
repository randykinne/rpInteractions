package RandomPvP.Core.Player.Gizmo.GUIs;

import RandomPvP.Core.Player.Inventory.InventoryType;
import RandomPvP.Core.Player.Inventory.RInventory;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class MainPage {

    private static RInventory inv;

    public static void build() {
        inv = new RInventory(54, InventoryType.MENU, "Gizmos - Home");
        {
            //TODO - Add sections
        }
    }

    public static RInventory getInventory() {
        return inv;
    }
}
