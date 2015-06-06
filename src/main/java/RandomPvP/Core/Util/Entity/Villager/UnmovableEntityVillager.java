package RandomPvP.Core.Util.Entity.Villager;

import net.minecraft.server.v1_7_R4.*;
import net.minecraft.server.v1_7_R4.Entity;
import org.bukkit.entity.*;

import java.lang.reflect.Field;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class UnmovableEntityVillager extends EntityVillager {

    public UnmovableEntityVillager(World world) {
        super(world);
        this.getAttributeInstance(GenericAttributes.d).setValue(0D);
        overridePathfinding();
    }

    private void overridePathfinding() {
        //clear pathfinding
        List goalB = (List)getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        //set it
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        //this.goalSelector.a(1, new PathfinderGoalAvoidTarget(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.goalSelector.a(1, new PathfinderGoalTradeWithPlayer(this));
        this.goalSelector.a(1, new PathfinderGoalLookAtTradingPlayer(this));
        this.goalSelector.a(2, new PathfinderGoalMoveIndoors(this));
        this.goalSelector.a(3, new PathfinderGoalRestrictOpenDoor(this));
        this.goalSelector.a(4, new PathfinderGoalOpenDoor(this, true));
        this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 0.6D));
        this.goalSelector.a(6, new PathfinderGoalMakeLove(this));
        this.goalSelector.a(7, new PathfinderGoalTakeFlower(this));
        //this.goalSelector.a(9, new PathfinderGoalInteract(this, EntityHuman.class, 3.0F, 1.0F));
        //this.goalSelector.a(9, new PathfinderGoalInteractVillagers(this));
        this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 0.6D));
        //this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
    }

    private Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;

        try
        {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return o;
    }

    public void setName(String name) {
        setCustomName(name);
    }

    public org.bukkit.entity.Entity getAsBukkit() {
        return getBukkitEntity();
    }

    public void setH(double d) {
        setH(d);
    }

    @Override
    protected String t() {
        return "";
    }

    @Override
    protected String aT() {
        return "";
    }

    @Override
    protected String aU() {
        return "";
    }

    @Override
    public boolean a(EntityHuman var1) { return false; }

    @Override
    public EntityAgeable createChild(EntityAgeable entityAgeable) { return null; }

}
