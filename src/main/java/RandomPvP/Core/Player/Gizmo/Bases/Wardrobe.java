package RandomPvP.Core.Player.Gizmo.Bases;

import RandomPvP.Core.Player.Gizmo.GizmoManager;
import org.bukkit.inventory.ItemStack;
/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public abstract class Wardrobe extends GizmoBase {

    public Wardrobe() {
        GizmoManager.getManager().addWardrobe(this);
    }

    public abstract ItemStack getHelmet();

    public abstract ItemStack getChest();

    public abstract ItemStack getPants();

    public abstract ItemStack getBoots();

}
