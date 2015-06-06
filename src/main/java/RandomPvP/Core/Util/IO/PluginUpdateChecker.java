package RandomPvP.Core.Util.IO;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.NetworkUtil;
import net.minecraft.util.org.apache.commons.codec.digest.DigestUtils;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class PluginUpdateChecker {

    public static void checkForUpdate(final String pluginName, final String link) {
        new Thread() {
            public void run() {
                try {
                    File onServer = new File("plugins" + File.separator + pluginName + ".jar");
                    File downloaded = new File("plugins" + File.separator + "UpdateChecker-" + pluginName + ".jar");

                    FileUtils.copyURLToFile(new URL(link), downloaded, 5000, 5000);

                    if(downloaded.exists()) {
                        if(!(DigestUtils.md5Hex(new FileInputStream(onServer))).equals(DigestUtils.md5Hex(new FileInputStream(downloaded)))) {
                            update(pluginName, link);
                        }
                    }

                    downloaded.delete();
                } catch (Exception ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public static void update(final String pluginName, final String link) {
        new Thread() {
            public void run() {
                try {
                    File newFile = new File("plugins" + File.separator + "PluginUpdater-" + pluginName + ".jar");

                    FileUtils.copyURLToFile(new URL(link), newFile, 5000, 5000);

                    if(newFile.exists()) {
                        for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
                            pl.getPlayer().kickPlayer("Server updating!");
                        }

                        unloadPlugin(Bukkit.getPluginManager().getPlugin(pluginName));
                        new File("plugins" + File.separator + pluginName + ".jar").delete();
                        newFile.renameTo(new File("plugins" + File.separator + pluginName + ".jar"));

                        NetworkUtil.restart();
                    }
                } catch (IOException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    //Taken from PlugMan
    private static boolean unloadPlugin(Plugin plugin)
    {
        try
        {
            plugin.getClass().getClassLoader().getResources("*");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        PluginManager pm = Bukkit.getServer().getPluginManager();
        Map ln;
        List pl;
        try {
            Field lnF = pm.getClass().getDeclaredField("lookupNames");
            lnF.setAccessible(true);
            ln = (Map)lnF.get(pm);

            Field plF = pm.getClass().getDeclaredField("plugins");
            plF.setAccessible(true);
            pl = (List)plF.get(pm);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        SimpleCommandMap scm;
        try {
            Field f =  RPICore.getInstance().getServer().getClass().getDeclaredField("commandMap");
            {
                f.setAccessible(true);
            }
            scm = (SimpleCommandMap) f.get(RPICore.getInstance().getServer());
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return false;
        }

        Map<String, Command> kc;
        try {
            Field f = scm.getClass().getDeclaredField("knownCommands");
            {
                f.setAccessible(true);
            }
            kc = (Map<String, Command>) f.get(scm);
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return false;
        }

        Iterator it = kc.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if ((entry.getValue() instanceof PluginCommand)) {
                PluginCommand c = (PluginCommand)entry.getValue();
                if (c.getPlugin().getName().equalsIgnoreCase(plugin.getName())) {
                    c.unregister(scm);
                    it.remove();
                }
            }
        }

        pm.disablePlugin(plugin);
        ln.remove(plugin.getName());
        pl.remove(plugin);

        JavaPluginLoader jpl = (JavaPluginLoader) plugin.getPluginLoader();
        Field loadersF;
        try {
            loadersF = jpl.getClass().getDeclaredField("loaders");
            {
                loadersF.setAccessible(true);
            }
        } catch (NoSuchFieldException ex) {
            NetworkUtil.handleError(ex);
            return false;
        }
        try
        {
            Map loaderMap = (Map)loadersF.get(jpl);
            loaderMap.remove(plugin.getDescription().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
