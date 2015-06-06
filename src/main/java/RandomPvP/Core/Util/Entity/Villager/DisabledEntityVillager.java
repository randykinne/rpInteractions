package RandomPvP.Core.Util.Entity.Villager;

import net.minecraft.server.v1_7_R4.*;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class DisabledEntityVillager extends UnmovableEntityVillager {

    public DisabledEntityVillager(World world) {
        super(world);

    }

    @Override
    public void collide(Entity e) {}

    @Override
    public void g(double d1, double d2, double d3) {}

    @Override
    public void die(DamageSource s) {}

    @Override
    protected boolean d(final DamageSource d, float f) { return false; }

}
