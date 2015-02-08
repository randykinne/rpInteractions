package RandomPvP.Core.Util.Tab;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.injector.BukkitUnwrapper;
import com.comphenix.protocol.injector.PacketConstructor;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.FieldUtils;
import com.comphenix.protocol.reflect.FuzzyReflection;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HidePlayerList  {
    // Copy of the player list packet
    private class PlayerListItem {
        public String name;
        public boolean online;
    }

    private PacketListener overrideListener;
    private ProtocolManager manager;

    // Used to construct packets
    private PacketConstructor playerListConstructor;

    // Players to hide
    private Set<String> hiddenPlayers = new HashSet<String>();

    // The current list of visible players
    private Set<String> visiblePlayers = new HashSet<String>();

    // To get the ping
    private Field pingField;

    /**
     * Start the player list hook.
     * @param plugin - owner plugin.
     */
    public HidePlayerList (RPICore plugin) {
        this.overrideListener = new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                try {
                    PlayerListItem item = getPlayerListInfo(event);

                    // Overridden player list?
                    if (hiddenPlayers.contains(item.name) && item.online) {
                        // Tell the client to remove this player instead
                        event.getPacket().getSpecificModifier(boolean.class).write(0, false);
                        item.online = false;
                    }

                    // Update list
                    if (item.online)
                        visiblePlayers.add(item.name);
                    else
                        visiblePlayers.remove(item.name);

                } catch (FieldAccessException e) {
                    e.printStackTrace();
                }
            }
        };
        this.manager = ProtocolLibrary.getProtocolManager();

        // Add every current player
        for (RPlayer player : PlayerManager.getInstance().getOnlinePlayers()) {
            if (!player.getTeam().isHidden()) {
                visiblePlayers.add(player.getPlayer().getPlayerListName());
            } else {
                hiddenPlayers.add(player.getPlayer().getPlayerListName());
            }
        }
    }

    // Read the player list information
    private PlayerListItem getPlayerListInfo(PacketEvent event) throws FieldAccessException {
        PacketContainer packet = event.getPacket();
        PlayerListItem result = new PlayerListItem();

        result.name = packet.getSpecificModifier(String.class).read(0);
        result.online = packet.getSpecificModifier(boolean.class).read(0);
        return result;
    }

    /**
     * Start the hook.
     */
    public void register() {
        manager.addPacketListener(overrideListener);
    }

    /**
     * Hide a player from the list.
     * @param player - the player to hide from the list.
     * @return TRUE if the player was not previously hidden, FALSE otherwise.
     */

    public boolean hidePlayer(Player player) {
        String name = player.getPlayerListName();
        boolean success = hiddenPlayers.add(name);

        // Remove it?
        if (visiblePlayers.contains(name)) {
            sendListPacket(player, false);
            visiblePlayers.remove(name);
        }
        return success;
    }

    /**
     * Show a player on the list.
     * @param player - the player to show on the list.
     * @return TRUE if the player was previously hidden, FALSE otherwise.
     */
    public boolean showPlayer(Player player) {
        String name = player.getPlayerListName();
        boolean success = hiddenPlayers.remove(name);

        // Add it?
        if (!visiblePlayers.contains(name)) {
            sendListPacket(player, true);
            visiblePlayers.add(name);
        }
        return success;
    }

    /**
     * Determine if a given player is visible in the player list.
     * @param player - the player to check.
     * @return TRUE if it is, FALSE otherwise.
     */
    public boolean isVisible(Player player) {
        return visiblePlayers.contains(player.getName());
    }

    /**
     * Retrieve the current ping value from a player.
     * @param player - player to retrieve.
     * @return The ping value.
     * @throws IllegalAccessException Unable to read the ping value due to a security limitation.
     */
    public int getPlayerPing(Player player) throws IllegalAccessException {
        BukkitUnwrapper unwrapper = new BukkitUnwrapper();
        Object entity = unwrapper.unwrapItem(player);

        // Next, get the "ping" field
        if (pingField == null) {
            pingField = FuzzyReflection.fromObject(entity).getFieldByName("ping");
        }

        return (Integer) FieldUtils.readField(pingField, entity) / 2;
    }

    private void sendListPacket(Player player, boolean visible) {

        String name = player.getPlayerListName();

        if (playerListConstructor == null) {
            // REQUIRES 1.4.2
            playerListConstructor = manager.createPacketConstructor(PacketType.Play.Server.PLAYER_INFO, "", false, (int) 0);
        }

        try {
            PacketContainer packet = playerListConstructor.createPacket(name, visible, getPlayerPing(player));

            // Just broadcast it
            for (RPlayer reciever : PlayerManager.getInstance().getOnlinePlayers()) {
                manager.sendServerPacket(reciever.getPlayer(), packet);
            }

        } catch (FieldAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve every hidden player.
     * @return Every hidden player.
     */
    public Set<String> getHiddenPlayers() {
        return Collections.unmodifiableSet(hiddenPlayers);
    }

    /**
     * Cleanup this player list hook.
     */
    public void cleanupAll() {
        if (overrideListener != null) {
            manager.removePacketListener(overrideListener);
            overrideListener = null;
        }
    }
}