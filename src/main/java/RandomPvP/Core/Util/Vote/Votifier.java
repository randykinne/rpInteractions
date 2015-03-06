package RandomPvP.Core.Util.Vote;

import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.Vote.crypto.RSAIO;
import RandomPvP.Core.Util.Vote.crypto.RSAKeygen;
import RandomPvP.Core.Util.Vote.model.ListenerLoader;
import RandomPvP.Core.Util.Vote.model.VoteListener;
import RandomPvP.Core.Util.Vote.net.VoteReceiver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Votifier
{
    private static RPICore c;
    private static final Logger LOG = Logger.getLogger("Votifier");
    private static final String logPrefix = "[Votifier] ";
    private static Votifier instance;
    private String version;
    private final List<VoteListener> listeners = new ArrayList();
    private VoteReceiver voteReceiver;
    private KeyPair keyPair;
    private boolean debug;

    public Votifier(RPICore core) {
        c = core;
    }

    public void onEnable()
    {
        instance = this;

        this.version = "1.9"; //NEEDS TO BE EDITED WHEN VOTIFIER VERSION CHANGES!!!

        if (!c.getDataFolder().exists()) {
            c.getDataFolder().mkdir();
        }
        File config = new File(c.getDataFolder() + "/voteconfig.yml");
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(config);
        File rsaDirectory = new File(c.getDataFolder() + "/votersa");

        String hostAddr = Bukkit.getServer().getIp();
        if ((hostAddr == null) || (hostAddr.length() == 0)) {
            hostAddr = "0.0.0.0";
        }

        if (!config.exists()) {
            try
            {
                LOG.info("Configuring Votifier for the first time...");

                config.createNewFile();

                cfg.set("host", hostAddr);
                cfg.set("port", Integer.valueOf(9999));
                cfg.set("debug", Boolean.valueOf(false));

                LOG.info("------------------------------------------------------------------------------");
                LOG.info("Assigning Votifier to listen on port 8192. If you are hosting Craftbukkit on a");
                LOG.info("shared server please check with your hosting provider to verify that this port");
                LOG.info("is available for your use. Chances are that your hosting provider will assign");
                LOG.info("a different port, which you need to specify in config.yml");
                LOG.info("------------------------------------------------------------------------------");

                cfg.set("listener_folder", "removed in RPI version");
                cfg.save(config);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error creating configuration file", ex);
                gracefulExit();
                return;
            }
        }
        else {
            cfg = YamlConfiguration.loadConfiguration(config);
        }

        try
        {
            if (!rsaDirectory.exists()) {
                rsaDirectory.mkdir();
                this.keyPair = RSAKeygen.generate(2048);
                RSAIO.save(rsaDirectory, this.keyPair);
            } else {
                this.keyPair = RSAIO.load(rsaDirectory);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error reading configuration file or RSA keys", ex);

            gracefulExit();
            return;
        }

        String host = cfg.getString("host", hostAddr);
        int port = cfg.getInt("port", 9999);
        this.debug = cfg.getBoolean("debug", false);
        if (this.debug)
            LOG.info("DEBUG mode enabled!");
        try
        {
            this.voteReceiver = new VoteReceiver(this, host, port);
            this.voteReceiver.start();

            LOG.info("Votifier enabled.");
        } catch (Exception ex) {
            gracefulExit();
            return;
        }
    }

    public void onDisable()
    {
        if (this.voteReceiver != null) {
            this.voteReceiver.shutdown();
        }
        LOG.info("Votifier disabled.");
    }

    private void gracefulExit() {
        LOG.log(Level.SEVERE, "Votifier did not initialize properly!");
    }

    public static Votifier getInstance()
    {
        return RPICore.getInstance().getVote();
    }

    public String getVersion()
    {
        return this.version;
    }

    public List<VoteListener> getListeners()
    {
        return this.listeners;
    }

    public VoteReceiver getVoteReceiver()
    {
        return this.voteReceiver;
    }

    public KeyPair getKeyPair()
    {
        return this.keyPair;
    }

    public boolean isDebug() {
        return this.debug;
    }

    static
    {
        LOG.setFilter(new LogFilter("[Votifier] "));
    }
}
