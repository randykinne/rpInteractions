package RandomPvP.Core.Util.MotdData;

import net.minecraft.server.v1_7_R4.MinecraftServer;

import java.lang.reflect.Field;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class MotdSetter {

    public void setMotd(String motd) {
        try {
            Field f = MinecraftServer.class.getDeclaredField("motd");
            f.setAccessible(true);
            f.set(MinecraftServer.getServer(), motd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
