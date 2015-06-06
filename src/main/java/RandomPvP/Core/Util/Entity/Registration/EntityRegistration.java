package RandomPvP.Core.Util.Entity.Registration;

import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Entity.Villager.UnmovableEntityVillager;
import RandomPvP.Core.Util.Entity.Villager.GameVillager.GameVillager;
import RandomPvP.Core.Util.Entity.Villager.ShopVillager.ShopVillager;
import RandomPvP.Core.Util.Entity.Villager.DisabledEntityVillager;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityTypes;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.lang.reflect.Field;
import java.util.*;

public enum EntityRegistration implements Listener {

    //NAME("Entity name", Entity ID, custom.class);
    LISTENER("REGISTERS LISTENER", 0, null), //we register the listener with this
    GAME_VILLAGER("Villager", 120, GameVillager.class),
    SHOP_VILLAGER("Villager", 120, ShopVillager.class),
    DISABLED_VILLAGER("Villager", 120, UnmovableEntityVillager.class),
    UNMOVABLE_VILLAGER("Villager", 120, DisabledEntityVillager.class);

    private static HashMap<Entity, Location> loaded = new HashMap<>();
    private static List<Chunk> saveEntities = new ArrayList<>();
    private static HashMap<List<Integer>, List<Entity>> chunkData = new HashMap<>();

    private EntityRegistration(String name, int id, Class<? extends Entity> custom)
    {
        if(name.equals("REGISTERS LISTENER")) {
            RPICore.getInstance().addListener(this);
        } else {
            addToMaps(custom, name, id);
        }
    }

    public static void spawnEntity(Entity entity, Location loc)
    {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        ((LivingEntity)entity.getBukkitEntity()).setRemoveWhenFarAway(false);

        if(!saveEntities.contains(loc.getChunk())) saveEntities.add(loc.getChunk());

        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        System.out.println(entity.getId());

        loaded.put(entity, loc);
        //sendPacket();
    }

    /*
    public static void sendPacket() {
        for(RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            sendPacket(pl);
        }
    }

    public static void sendPacket(RPlayer pl) {
        for(Entity en : loaded.keySet()) {
            Location loc = loaded.get(en);
            int id;
            {
                try {
                    Field f = net.minecraft.server.v1_8_R2.Entity.class.getDeclaredField("id");
                    f.setAccessible(true);
                    id = (int)f.get(en);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook p = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(
                    id,
                    (byte) loc.getX(),
                    (byte) loc.getY(),
                    (byte) loc.getZ(),
                    (byte) loc.getYaw(),
                    (byte) loc.getPitch(),
                    en.getBukkitEntity().isOnGround()
            );
            System.out.println("Packet sent - " + loc.getYaw());
            ((CraftPlayer) pl.getPlayer()).getHandle().playerConnection.sendPacket(p);
        }
    }
    */

    public static Object getPrivateField(String fieldName, Class clazz, Object object)
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


    private static void addToMaps(Class clazz, String name, int id)
    {
        //getPrivateField is the method from above.
        //Remove the lines with // in front of them if you want to override default entities (You'd have to remove the default entity from the map first though).
        //((Map)getPrivateField("c", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, clazz);
        ((Map)getPrivateField("d", EntityTypes.class, null)).put(clazz, name);
        //((Map)getPrivateField("e", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map)getPrivateField("f", EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
        //((Map)getPrivateField("g", net.minecraft.server.v1_7_R4.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }

    public static void clearAll() {
        for(Entity e : loaded.keySet()) {
            e.getBukkitEntity().remove();
        }

        saveEntities.clear();
        chunkData.clear();
        loaded.clear();
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        if(saveEntities.contains(e.getChunk())) {
            List<Entity> entities = new ArrayList<>();
            {
                for(org.bukkit.entity.Entity en : e.getChunk().getEntities()) {
                    Entity entity = ((CraftEntity) en).getHandle();
                    if(loaded.containsKey(entity)) {
                        entities.add(entity);
                    }
                }
            }

            List<Integer> locs = new ArrayList<>();
            {
                locs.add(e.getChunk().getX());
                locs.add(e.getChunk().getZ());
            }

            chunkData.put(locs, entities);
            for(Entity en : entities) {
                en.getBukkitEntity().remove();
                loaded.remove(en);
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent e) {
        List<Integer> locs = new ArrayList<>();
        {
            locs.add(e.getChunk().getX());
            locs.add(e.getChunk().getZ());
        }

        if(chunkData.containsKey(locs)) {
            for(Entity en : chunkData.get(locs)) {
                /*
                en.getClass().getConstructor()
                Entity toSpawn = en;
                {
                    try {
                        Field get1 = Entity.class.getDeclaredField("entityCount");
                        {
                            get1.setAccessible(true);
                        }
                        int entityCount = (int) get1.get(toSpawn);

                        Field set1 = Entity.class.getDeclaredField("uniqueID");
                        {
                            set1.setAccessible(true);
                        }

                        Field set2 = Entity.class.getDeclaredField("id");
                        {
                            set2.setAccessible(true);
                        }

                        set1.set(toSpawn, MathHelper.a(new Random())); //set uuid
                        get1.set(toSpawn, entityCount++); //set entity id
                        get1.set(toSpawn, entityCount); //set entity count
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }


                System.out.println(toSpawn.getBukkitEntity().getLocation());
                spawnEntity(toSpawn, toSpawn.getBukkitEntity().getLocation());
                */

                if(en instanceof ShopVillager) {
                    ShopVillager old = (ShopVillager) en;
                    ShopVillager v = new ShopVillager(en.getBukkitEntity().getLocation(), old.getName(), old.getGui(), old.isBetaGame());
                    {
                        v.getBukkitEntity().setPassenger(en.getBukkitEntity().getPassenger());
                        v.setInvisible(en.isInvisible());
                        v.setAge(((ShopVillager) en).getAge());
                        v.setCustomName(((ShopVillager) en).getCustomName());
                    }
                } else if(en instanceof GameVillager) {
                    GameVillager old = (GameVillager) en;
                    GameVillager v = new GameVillager(en.getBukkitEntity().getLocation(), old.getGamename(), old.getInventory());
                    {
                        v.getBukkitEntity().setPassenger(en.getBukkitEntity().getPassenger());
                        v.setInvisible(en.isInvisible());
                        v.setAge(((GameVillager) en).getAge());
                        v.setCustomName(((GameVillager) en).getCustomName());
                    }
                } else if(en instanceof UnmovableEntityVillager) {
                    UnmovableEntityVillager v = new UnmovableEntityVillager(((CraftWorld)en.getBukkitEntity().getLocation().getWorld()).getHandle());
                    {
                        v.getBukkitEntity().setVelocity(en.getBukkitEntity().getVelocity());
                        v.getBukkitEntity().setMomentum(en.getBukkitEntity().getMomentum());
                        v.getBukkitEntity().setLastDamageCause(en.getBukkitEntity().getLastDamageCause());
                        v.getBukkitEntity().setPassenger(en.getBukkitEntity().getPassenger());
                        v.setAge(((UnmovableEntityVillager) en).getAge());
                        v.setCustomName(((UnmovableEntityVillager) en).getCustomName());
                    }
                } else if(en instanceof DisabledEntityVillager) {
                    DisabledEntityVillager v = new DisabledEntityVillager(((CraftWorld)en.getBukkitEntity().getLocation().getWorld()).getHandle());
                    {
                        v.getBukkitEntity().setVelocity(en.getBukkitEntity().getVelocity());
                        v.getBukkitEntity().setMomentum(en.getBukkitEntity().getMomentum());
                        v.getBukkitEntity().setPassenger(en.getBukkitEntity().getPassenger());
                        v.setInvisible(en.isInvisible());
                        v.setAge(((DisabledEntityVillager) en).getAge());
                        v.setCustomName(((DisabledEntityVillager) en).getCustomName());
                    }
                }

            }

            chunkData.remove(locs);
        }
    }

}
