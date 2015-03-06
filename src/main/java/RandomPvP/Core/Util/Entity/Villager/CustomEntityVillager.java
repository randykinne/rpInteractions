package RandomPvP.Core.Util.Entity.Villager;

import net.minecraft.server.v1_7_R4.*;

import java.lang.reflect.Field;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class CustomEntityVillager extends EntityVillager {

    public CustomEntityVillager(net.minecraft.server.v1_7_R4.World world) {
        super(world);
        this.getAttributeInstance(GenericAttributes.d).setValue(0D);
        disablePathfinding();
    }

    public void disablePathfinding() {
        try {
            Field goal = this.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("goalSelector");
            goal.setAccessible(true);

            goal.set(this, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 0, 0));
        } catch (Exception ignored){}
    }

    @Override
    public boolean a(EntityHuman var1) { return false; }

    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) { return null; }

    @Override
    public void collide(Entity e) {}

    @Override
    public void g(double d1, double d2, double d3) {}

    @Override
    public void die(DamageSource s) {}

    @Override
    protected boolean d(final DamageSource d, float f) { return false; }

}
