package RandomPvP.Core.System.Info;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.NumberUtil;
import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;

import java.lang.management.ManagementFactory;


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
public class ServerAdminInfo {

    private static String totalSystemRAM;
    private static String totalUsedSystemRAM;
    private static OperatingSystemMXBean os = (OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();
    private static final int mb = 1048576;

    public static void updateSystemInfo() {
        totalUsedSystemRAM = NumberUtil.humanReadableByteCount(os.getTotalPhysicalMemorySize() - os.getFreePhysicalMemorySize(), true);
        totalSystemRAM = NumberUtil.humanReadableByteCount(os.getTotalPhysicalMemorySize(), true);
    }

    public static long getSystemUptime() {
        return  System.currentTimeMillis() - RPICore.serverStart;
    }

    public static void sendServerInfo(RPlayer pl) throws Exception {
        updateSystemInfo();
        pl.message("§2§m----------------------------------------------------");
        pl.message("\n");
        pl.message("§2§l>> §a§lSERVER INFO: " + Bukkit.getServerName());
        pl.message("§2§l>> §aMemory§7: " + totalUsedSystemRAM + " / " + totalSystemRAM);
        pl.message("§2§l>> §aUptime§7: " + NumberUtil.translateDuration(getSystemUptime()));
        pl.message("\n");
        pl.message("§2§m----------------------------------------------------");
    }
}
