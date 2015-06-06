package RandomPvP.Core.Util.IO;

import RandomPvP.Core.RPICore;
import net.minecraft.util.org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class LibLoader {

    private LibLoader(){}

    public static void loadLib(String name, String link) {
        try {
            File libs = new File(RPICore.getInstance().getDataFolder(), "libs");
            {
                if(!libs.exists()) libs.mkdir();
            }
            File lib = new File(libs, name);
            {
                if(!lib.exists()) FileUtils.copyURLToFile(new URL(link), new File(libs, name), 5000, 5000);
            }

            if (!lib.exists()) {
                extractFromJar(lib.getName(), lib.getAbsolutePath());
            }
            if (!lib.exists()) {
                Bukkit.getLogger().warning(
                        "ERROR >>> Could find/get lib: " + lib.getName());
                return;
            }
            addClassPath(getJarUrl(lib));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean extractFromJar(final String fileName,
                                         final String dest) throws IOException {
        if (getRunningJar() == null) {
            return false;
        }
        final File file = new File(dest);
        if (file.isDirectory()) {
            file.mkdir();
            return false;
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        final JarFile jar = getRunningJar();
        final Enumeration<JarEntry> e = jar.entries();
        while (e.hasMoreElements()) {
            final JarEntry je = e.nextElement();
            if (!je.getName().contains(fileName)) {
                continue;
            }
            final InputStream in = new BufferedInputStream(
                    jar.getInputStream(je));
            final OutputStream out = new BufferedOutputStream(
                    new FileOutputStream(file));
            copyInputStream(in, out);
            jar.close();
            return true;
        }
        jar.close();
        return false;
    }

    private static void copyInputStream(final InputStream in,
                                              final OutputStream out) throws IOException {
        try {
            final byte[] buff = new byte[4096];
            int n;
            while ((n = in.read(buff)) > 0) {
                out.write(buff, 0, n);
            }
        } finally {
            out.flush();
            out.close();
            in.close();
        }
    }

    public static URL getJarUrl(final File file) throws IOException {
        return new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
    }

    public static JarFile getRunningJar() throws IOException {
        if (!RUNNING_FROM_JAR) {
            return null; // null if not running from jar
        }
        String path = new File(LibLoader.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath()).getAbsolutePath();
        path = URLDecoder.decode(path, "UTF-8");
        return new JarFile(path);
    }

    private static boolean RUNNING_FROM_JAR = false;

    static {
        final URL resource = LibLoader.class.getClassLoader()
                .getResource("plugin.yml");
        if (resource != null) {
            RUNNING_FROM_JAR = true;
        }
    }

    private static void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url
                    + " to system classloader");
        }
    }

}
