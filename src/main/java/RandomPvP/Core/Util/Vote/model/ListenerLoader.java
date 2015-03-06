package RandomPvP.Core.Util.Vote.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerLoader
{
    private static final Logger LOG = Logger.getLogger("Votifier");

    public static List<VoteListener> load(String directory)
    {
        List listeners = new ArrayList();
        File dir = new File(directory);

        if (!dir.exists()) {
            LOG.log(Level.WARNING, "No listeners loaded! Cannot find listener directory '" + dir + "' ");

            return listeners;
        }

        ClassLoader loader;
        try
        {
            loader = new URLClassLoader(new URL[] { dir.toURI().toURL() }, VoteListener.class.getClassLoader());
        }
        catch (MalformedURLException ex) {
            LOG.log(Level.SEVERE, "Error while configuring listener class loader", ex);

            return listeners;
        }
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".class"))
            {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                try
                {
                    Class clazz = loader.loadClass(name);
                    Object object = clazz.newInstance();
                    if (!(object instanceof VoteListener)) {
                        LOG.info("Not a vote listener: " + clazz.getSimpleName());
                    }
                    else {
                        VoteListener listener = (VoteListener)object;
                        listeners.add(listener);
                        LOG.info("Loaded vote listener: " + listener.getClass().getSimpleName());
                    }

                }
                catch (Exception ex)
                {
                    LOG.log(Level.WARNING, "Error loading '" + name + "' listener! Listener disabled.");
                }
                catch (Error ex) {
                    LOG.log(Level.WARNING, "Error loading '" + name + "' listener! Listener disabled.");
                }
            }
        }
        return listeners;
    }
}
