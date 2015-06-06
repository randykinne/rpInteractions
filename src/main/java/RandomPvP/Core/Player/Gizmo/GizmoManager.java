package RandomPvP.Core.Player.Gizmo;

import RandomPvP.Core.Player.Gizmo.Bases.GizmoBase;
import RandomPvP.Core.Player.Gizmo.Bases.Wardrobe;
import RandomPvP.Core.Player.Gizmo.GUIs.MainPage;
import RandomPvP.Core.Server.General.Shop.ShopItem;
import RandomPvP.Core.Server.General.Shop.ShopManager;
import org.bukkit.Material;

import java.util.*;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class GizmoManager {

    private static GizmoManager manager = new GizmoManager();
    private List<Wardrobe> wardrobes = new ArrayList<>();

    private GizmoManager() {
        ShopManager.getInstance().createItNotExists("gizmos");
        MainPage.build();
    }

    public void addAsShopItem(final GizmoBase base) {
        ShopManager.getInstance().addItem(new ShopItem() {
            @Override
            public String getName() {
                return base.getDisplayName();
            }

            @Override
            public String getDisplayName() {
                return "storage";
            }

            @Override
            public List<String> getDescription() {
                return Arrays.asList("storage");
            }

            @Override
            public Material getMaterial() {
                return Material.BEDROCK;
            }

            @Override
            public boolean freeForDonors() {
                return false;
            }

            @Override
            public int getPrice() {
                return -1;
            }
        }, "gizmos");
    }

    public void addWardrobe(Wardrobe wardrobe) {
        wardrobes.add(wardrobe);
    }

    public void removeWardrobe(Wardrobe wardrobe) {
        wardrobes.remove(wardrobe);
    }

    public Collection<Wardrobe> getWardrobes() {
        return Collections.unmodifiableCollection(wardrobes);
    }

    public static GizmoManager getManager() {
        return manager;
    }
}
