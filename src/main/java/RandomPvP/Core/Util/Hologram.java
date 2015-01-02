package RandomPvP.Core.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.RPICore;
import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Hologram {

    private static final double distance = 0.23;

    private Location location;
    private String[] lines;

    private EntityWitherSkull[] skulls;
    private EntityHorse[] horses;

    private final List<Integer> ids = new ArrayList<Integer>();

    public Hologram(Location location, String... lines) {
        this.location = location.clone();
        this.lines = lines;
        skulls = new EntityWitherSkull[lines.length];
        horses = new EntityHorse[lines.length];
    }

    public void destroy() {

        int[] ints = new int[this.ids.size()];
        for (int j = 0; j < ints.length; j++) {
            ints[j] = this.ids.get(j);
        }

        for(Entity ent : this.location.getWorld().getEntities()){
            if(ent.getType() == EntityType.WITHER_SKULL){
                // System.out.println("WITHER_SKULL @ " + ent.getLocation().toString());
                if(ent.getLocation().distanceSquared(this.location) <= 4){
                    ent.remove();
                }
            }
        }

        final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(ints);

        Bukkit.getScheduler().runTaskAsynchronously(RPICore.getInstance(), new Runnable(){
            public void run(){
                for (RPlayer pl : RPlayerManager.getInstance().getOnlinePlayers()) {
                    Player player = pl.getPlayer();
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }
        });

        this.location = null;

    }

    public void show() {
        show(null);
    }

    public void show(Player single_target) {
        Location first = location.clone().add(0, (this.lines.length / 2) * distance, 0);
        for (int i = 0; i < this.lines.length; i++) {
            this.ids.addAll(showLine(first.clone(), i, single_target));
            first.subtract(0, distance, 0);
        }
    }

    private synchronized List<Integer> showLine(final Location loc, final int index, final Player single_target) {
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        if (null == skulls[index]) {
            skulls[index] = new EntityWitherSkull(world);
        }
        skulls[index].setLocation(loc.getX(), loc.getY() + 1 + 55, loc.getZ(), 0, 0);
        // ((CraftWorld) loc.getWorld()).getHandle().addEntity(skull);
        final PacketPlayOutSpawnEntity packet_skull = new PacketPlayOutSpawnEntity(skulls[index], 66);

        if (null == horses[index]) {
            horses[index] = new EntityHorse(world);
        }
        horses[index].setLocation(loc.getX(), loc.getY() + 55, loc.getZ(), 0, 0);
        horses[index].setAge(-1700000);
        horses[index].setCustomName(lines[index]);
        horses[index].setCustomNameVisible(true);
        final PacketPlayOutSpawnEntityLiving packedt = new PacketPlayOutSpawnEntityLiving(horses[index]);

        Bukkit.getScheduler().runTaskAsynchronously(RPICore.getInstance(), new Runnable(){
            public void run(){
                if(single_target == null){
                    for (Player player : loc.getWorld().getPlayers()) {
                        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
                        nmsPlayer.playerConnection.sendPacket(packedt);
                        nmsPlayer.playerConnection.sendPacket(packet_skull);
                        PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horses[index], skulls[index]);
                        nmsPlayer.playerConnection.sendPacket(pa);
                    }
                } else {
                    EntityPlayer nmsPlayer = ((CraftPlayer) single_target).getHandle();
                    nmsPlayer.playerConnection.sendPacket(packedt);
                    nmsPlayer.playerConnection.sendPacket(packet_skull);

                    PacketPlayOutAttachEntity pa = new PacketPlayOutAttachEntity(0, horses[index], skulls[index]);
                    nmsPlayer.playerConnection.sendPacket(pa);
                }
            }
        });
        return Arrays.asList(skulls[index].getId(), horses[index].getId());
    }

//    public void show(Location loc, long ticks, Player single_target) {
//        show(loc, single_target);
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                destroy();
//            }
//        }.runTaskLater(LobbyPlugin.getInstance(), ticks);
//    }

}