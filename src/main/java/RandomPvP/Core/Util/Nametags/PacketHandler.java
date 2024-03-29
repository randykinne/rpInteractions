package RandomPvP.Core.Util.Nametags;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * A small wrapper for the PacketPlayOutScoreboardTeam packet.
 *
 * This code is directly for NametagEdit and has been edited.
 */
public class PacketHandler {

    private Object packet;

    private static Class<?> packetType;

    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;

    private static String version = "";
    private static String fieldPrefix = "";
    private static String fieldSuffix = "";
    private static String fieldPlayers = "";
    private static String fieldTeamName = "";
    private static String fieldParamInt = "";
    private static String fieldPackOption = "";
    private static String fieldDisplayName = "";

    static {
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            Class<?> typeNMSPlayer = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            Class<?> typeCraftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Class<?> typePlayerConnection = Class.forName("net.minecraft.server." + version + ".PlayerConnection");

            getHandle = typeCraftPlayer.getMethod("getHandle");
            playerConnection = typeNMSPlayer.getField("playerConnection");
            sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"));

            if (version.startsWith("v1_8")) {
                fieldPrefix = "c";
                fieldSuffix = "d";
                fieldPlayers = "g";
                fieldTeamName = "a";
                fieldParamInt = "h";
                fieldPackOption = "i";
                fieldDisplayName = "b";

                RPICore.getInstance().getLogger().info("Loading with 1.8 support");
            } else {
                fieldPrefix = "c";
                fieldSuffix = "d";
                fieldPlayers = "e";
                fieldTeamName = "a";
                fieldParamInt = "f";
                fieldPackOption = "g";
                fieldDisplayName = "b";

                RPICore.getInstance().getLogger().info("Loading with 1.5-1.7 support");
            }

            if (version.startsWith("v1_5")) {
                packetType = Class.forName("net.minecraft.server." + version + ".Packet209SetScoreboardTeam");
            } else {
                packetType = Class.forName("net.minecraft.server." + version + ".PacketPlayOutScoreboardTeam");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PacketHandler(String name, String prefix, String suffix, Collection<?> players, int paramInteger) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {

        packet = packetType.newInstance();
        setField(fieldTeamName, name);
        setField(fieldParamInt, paramInteger);

        if (paramInteger == 0 || paramInteger == 2) {
            setField(fieldDisplayName, name);
            setField(fieldPrefix, prefix);
            setField(fieldSuffix, suffix);
            setField(fieldPackOption, 1);
        }

        if (paramInteger == 0) {
            addAll(players);
        }
    }

    public PacketHandler(String name, Collection<?> players, int paramInt) throws ClassNotFoundException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
        packet = packetType.newInstance();

        if (paramInt != 3 && paramInt != 4) {
            throw new IllegalArgumentException(
                    "Method must be join or leave for player constructor");
        }

        if (players == null || players.isEmpty()) {
            players = new ArrayList<String>();
        }

        setField(fieldTeamName, name);
        setField(fieldParamInt, paramInt);
        addAll(players);
    }

    public void sendToPlayer(Player bukkitPlayer) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException,
            NoSuchMethodException, NoSuchFieldException {
        Object player = getHandle.invoke(bukkitPlayer);

        Object connection = playerConnection.get(player);

        sendPacket.invoke(connection, packet);
    }

    private void setField(String field, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field f = packet.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(packet, value);
    }

    @SuppressWarnings("all")
    private void addAll(Collection<?> col) throws NoSuchFieldException, IllegalAccessException {
        Field f = packet.getClass().getDeclaredField(fieldPlayers);
        f.setAccessible(true);
        ((Collection) f.get(packet)).addAll(col);
    }
}
