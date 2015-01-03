package RandomPvP.Core.AntiCheat.KillAura;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
import RandomPvP.Core.AntiCheat.KillAura.Packet.WrapperPlayServerEntityDestroy;
import RandomPvP.Core.AntiCheat.KillAura.Packet.WrapperPlayServerNamedEntitySpawn;
import RandomPvP.Core.RPICore;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AuraCheck {
    public static final short[][] standing = {{0,1},{-1,0},{1,0},{0,-1},{1,1},{-1,-1},{-1,1},{1,-1}};
    public static final short[][] running = {{0,2},{-2,0},{2,0},{0,-2},{1,1},{2,2},{-1,-1},{-2,-2},{-1,1},{-2,2},{1,-1},{2,-2}};

    private final AntiAura plugin;
    private HashMap<Integer, Boolean> entitiesSpawned = new HashMap<>();
    private CommandSender invoker;
    private Player checked;
    private long started;
    private long finished = Long.MAX_VALUE;
    private int i = -1;
    private int z;


    public AuraCheck(AntiAura plugin, Player checked) {
        this.plugin = plugin;
        this.checked = checked;
    }

    public void invoke(CommandSender player, String type, boolean visOrInvisible, final Callback callback) {
        this.invoker = player;
        this.started = System.currentTimeMillis();

        if (type.equalsIgnoreCase("running")) {
            while (z < AntiAura.total) {
                z++;
                i++;
                if (i == 12) {
                    i = 0;
                }
                WrapperPlayServerNamedEntitySpawn wrapper = getWrapper(this.checked.getLocation().add(running[i][0], 0, running[i][1]).toVector(), plugin, visOrInvisible);
                entitiesSpawned.put(wrapper.getEntityID(), false);
                wrapper.sendPacket(this.checked);
            }
        } else {
            while (z < AntiAura.total) {
                z++;
                i++;
                if (i == 8) {
                    i = 0;
                }
                WrapperPlayServerNamedEntitySpawn wrapper = getWrapper(this.checked.getLocation().add(standing[i][0], 0, standing[i][1]).toVector(), plugin, visOrInvisible);
                entitiesSpawned.put(wrapper.getEntityID(), false);
                wrapper.sendPacket(this.checked);
            }
        }
        i = -1;

        new BukkitRunnable() {
            public void run() {
                AbstractMap.SimpleEntry<Integer, Integer> result = end();
                new AntiAura().remove(checked.getUniqueId());
                callback.done(started, finished, result, invoker, checked);
            }
        }.runTaskLater(RPICore.getInstance(), RPICore.getInstance().getConfig().getInt("settings.ticksToKill"));
    }

    public void markAsKilled(Integer val) {
        if (entitiesSpawned.containsKey(val)) {
            entitiesSpawned.put(val, true);
            kill(val).sendPacket(checked);
        }

        if (!entitiesSpawned.containsValue(false)) {
            this.finished = System.currentTimeMillis();
        }
    }

    public AbstractMap.SimpleEntry<Integer, Integer> end() {
        int killed = 0;
        for (Map.Entry<Integer, Boolean> entry : entitiesSpawned.entrySet()) {
            if (entry.getValue()) {
                killed++;
            } else if (checked.isOnline()) {
                kill(entry.getKey()).sendPacket(checked);
            }

        }
        int amount = entitiesSpawned.size();
        entitiesSpawned.clear();
        return new AbstractMap.SimpleEntry<>(killed, amount);

    }

    public static WrapperPlayServerNamedEntitySpawn getWrapper(Vector loc, AntiAura plugin, boolean visOrInvisible) {
        WrapperPlayServerNamedEntitySpawn wrapper = new WrapperPlayServerNamedEntitySpawn();
        wrapper.setEntityID(AntiAura.RANDOM.nextInt(20000));
        wrapper.setPosition(loc);
        wrapper.setPlayerUUID(UUID.randomUUID().toString());
        wrapper.setPlayerName(String.valueOf(AntiAura.RANDOM.nextInt()));
        wrapper.setYaw(0);
        wrapper.setPitch(-45);
        WrappedDataWatcher watcher = new WrappedDataWatcher();
        watcher.setObject(0, visOrInvisible ? (Byte) (byte) 0x20 : (byte) 0);
        watcher.setObject(6, (Float) (float) 0.5);
        watcher.setObject(11, (Byte) (byte) 1);
        wrapper.setMetadata(watcher);
        return wrapper;
    }

    public static WrapperPlayServerEntityDestroy kill(int entity) {
        WrapperPlayServerEntityDestroy wrapper = new WrapperPlayServerEntityDestroy();
        wrapper.setEntities(new int[]{entity});
        return wrapper;
    }

    public interface Callback {
        public void done(long started, long finished, AbstractMap.SimpleEntry<Integer, Integer> result, CommandSender invoker, Player target);
    }

}
