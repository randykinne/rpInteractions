package RandomPvP.Core.Util;

import RandomPvP.Core.Game.Team.Team;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

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
public class Broadcasts implements PluginMessageListener {

    public static void sendRankedBroadcast(Rank rank, boolean specific, boolean global, String message) {

        //message = "§8[" + rank.getColor() + rank.getName() + "§8] " + message;

        if (rank == Rank.MOD && !specific) {
            message = "§8(§6Staff§8) " + message;
        } else if (rank == Rank.VIP) {
            message = "§8(§eVIP§8) " + message;
        }

        for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            if (specific) {
                if (pl.getRank() == rank) {
                    pl.message(message);
                }
            } else {
                if (pl.getRank().has(rank)) {
                    if (rank == Rank.MOD) {
                        if (!pl.getStaff().hasSTFUEnabled()) {
                            pl.message(message);
                        }
                    } else {
                        pl.message(message);
                    }
                }
            }
        }

        if (global) {
            forwardString(rank.getName(), "ONLINE", message);
        }
    }

    public static void sendTeamBroadcast(Team team, boolean restricted, String message) {
        if (!restricted) Bukkit.broadcastMessage("§8(" + team.getColor() + team.getName() + "§8) " + message); else {

            for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
                if (pl.getTeam().getName().equalsIgnoreCase(team.getName())) {
                    pl.message("§8(" + team.getColor() + team.getName() + "§8) " + message);
                }
            }
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        try{
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
            String subchannel = in.readUTF();
            for (Rank rank : Rank.values()) {
                if (subchannel.equalsIgnoreCase(rank.getName())) {
                    short len = in.readShort();
                    byte[] data = new byte[len];
                    in.readFully(data);

                    //byte array to string
                    String s = new String(data);

                    sendRankedBroadcast(rank, false, false, s);
                }
            }

        } catch(Exception ex){
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

            if (PlayerManager.getInstance().getOnlinePlayers().size() > 0) {
                p = getRandomPlayer();
            }

            if (p != null) {
                p.sendPluginMessage(RPICore.getInstance(), "BungeeCord", b.toByteArray());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Player getRandomPlayer() {
        Player p = null;
        for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            if (pl.getPlayer().isOnline()) {
                p = pl.getPlayer();
                break;
            }
        }

        return p;
    }
}
