package RandomPvP.Core.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

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
public class ServerUtils {

    // https://github.com/ArthurMaker/MillenaryAPI/blob/master/src/Millenary/MillenaryAPI.java


    public static boolean restartServer(){
        System.out.println("Asking for restart..."); // "Requisitando restart..."
        shutdownServer();
        ProcessBuilder pb = new ProcessBuilder();
        String jarname = Bukkit.class.getResource("").getFile();
        jarname = jarname.substring(0, jarname.indexOf(".jar"));
        jarname = new File(jarname).getName()+".jar";
        List<String> a = ManagementFactory.getRuntimeMXBean().getInputArguments();
        List<String> e = new ArrayList<String>();
        e.add("java"); e.addAll(a); e.add("-jar"); e.add(jarname);
        pb.command(e);
        try {
            pb.start();
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean restartServer(String... custom_arguments){
        System.out.println("Asking for restart..."); // "Requisitando restart..."
        shutdownServer();
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(custom_arguments);
        try {
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void shutdownServer(){
        System.out.println("Turning off..."); // "Desligando servidor..."
        Bukkit.getServer().savePlayers();
        for(World w : Bukkit.getWorlds()) w.save();
        Bukkit.getServer().shutdown();
    }
}
