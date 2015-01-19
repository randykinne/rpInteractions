package RandomPvP.Core.Util;

import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.Calendar;

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
public class RPStaff implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("Chat")) {
            return;
        }

        try{
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subchannel = in.readUTF();
            if (subchannel.equals("Staff")) {
                short len = in.readShort();
                byte[] data = new byte[len];
                in.readFully(data);

                //bytearray to string
                String s = new String(data);

                sendStaffMessage(s, false);
            } else if (subchannel.equals("VIP")) {
                short len = in.readShort();
                byte[] data = new byte[len];
                in.readFully(data);

                //bytearraytostring
                String s= new String(data);

                sendVIPMessage(s, false);
            } else if (subchannel.equals("ABroadcast")) {
                short len = in.readShort();
                byte[] data = new byte[len];
                in.readFully(data);

                //byate array string thing
                String s= new String(data);

                sendAdminBroadcast(s, false);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void forwardString(String subchannel, String target, String s) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Forward");
            out.writeUTF(target);
            out.writeUTF(subchannel); // "customchannel" for example
            byte[] data = s.getBytes();
            out.writeShort(data.length);
            out.write(data);

            Player p = null;
            for (RPlayer pl :RPlayerManager.getInstance().getOnlinePlayers()) {
                if (pl.isStaff()) {
                    p = pl.getPlayer();
                    break;
                }
            }

            if (p == null) {
                for (RPlayer pl :RPlayerManager.getInstance().getOnlinePlayers()) {
                        p = pl.getPlayer();
                        break;
                }
            }

            p.sendPluginMessage(RPICore.getInstance(), "BungeeCord", b.toByteArray());
         }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendAdminBroadcast(String message, boolean isGlobal) {
        Bukkit.broadcastMessage("§4╔══════════════════════════════════");
        Bukkit.broadcastMessage("§4║ §4§l*-*-* ADMIN BROADCAST *-*-*");
        Bukkit.broadcastMessage("§4║ §7" + message);
        Bukkit.broadcastMessage("§4╚══════════════════════════════════");

        if (isGlobal) {
            forwardString("ABroadcast", "ALL", message);
        }
    }

    public static void sendStaffMessage(String message, boolean isGlobal) {
        for (RPlayer pl : RPlayerManager.getInstance().getOnlinePlayers()) {
            if (pl.isStaff() && !pl.hasSTFUEnabled()) {
                pl.message("§2§l>> §f" + message);
            }

            if (isGlobal) {
                forwardString("Staff", "ALL", message);
            }
        }
    }

    public static void sendVIPMessage(String message, boolean isGlobal) {
        for (RPlayer pl : RPlayerManager.getInstance().getOnlinePlayers()) {
            if (pl.isVIP() && !pl.hasSTFUEnabled()) {
                pl.message("§2§l>> §f" + message);
            }

            if (isGlobal) {
                forwardString("VIP", "ALL", message);
            }
        }
    }

    public static void nagStaffMembers() {
        new BukkitRunnable() {
            public void run() {
                if (RPlayerManager.getInstance().getOnlinePlayers().size() > 0) {
                    for (RPlayer pl : RPlayerManager.getInstance().getOnlinePlayers()) {
                        if (pl.isStaff()) {
                            if (pl.hasSTFUEnabled()) {
                                pl.message("§d§l>> §fYou have staff messages §c§lOFF§7.");
                                pl.message("§d§l>> §e/stfu §7to turn them §7back on.");
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(RPICore.getInstance(), 2400L, 2400L);
    }

    public static void questionIP(RPlayer pl) {
        /*
        pl.message("§8╔══════════════════════════════════");
        pl.message("§8║ §4§lIP NOTICE");
        pl.message("§8║ §7Your current IP has changed since the last time you were logged in.");
        pl.message("§8║ §7In order to continue, please enter your staff password.");
        pl.message("§8╚══════════════════════════════════");
        */
    }

    static File log;
    public static void log(String fileName, String player, CommandSender sender, String reason) throws IOException {
        log = new File(RPICore.getInstance().getDataFolder(), fileName);

        if (!log.exists())
        {
            System.out.println("[Staff] Creating new log file...");

            log.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(log, true);

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        Calendar c = Calendar.getInstance();

        String stringdate = c.getTime().toString();

        bufferedWriter.write("\n" + stringdate + sender.getName() + ": " + reason);

        bufferedWriter.close();
    }


}
