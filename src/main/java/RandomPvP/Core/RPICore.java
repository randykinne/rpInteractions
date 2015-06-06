package RandomPvP.Core;

import RandomPvP.Core.Chat.ChatHandler;
import RandomPvP.Core.Commands.Admin.*;
import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Commands.Game.*;
import RandomPvP.Core.Commands.Mod.*;
import RandomPvP.Core.Commands.Command.RCommandMap;
import RandomPvP.Core.Commands.Premium.ColorCmd;
import RandomPvP.Core.Commands.Prime.FlyCmd;
import RandomPvP.Core.Commands.Server.*;
import RandomPvP.Core.Commands.VIP.GoToCmd;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Listener.*;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Server.Game.GameManager;
import RandomPvP.Core.Server.Game.Team.TeamManager;
import RandomPvP.Core.Server.General.Messages;
import RandomPvP.Core.Server.General.Shop.ShopManager;
import RandomPvP.Core.Server.Punish.PunishmentManager;
import RandomPvP.Core.Util.Entity.Registration.EntityRegistration;
import RandomPvP.Core.Util.IO.LibLoader;
import RandomPvP.Core.Util.IO.PluginUpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
public final class RPICore extends JavaPlugin {

    private static RPICore instance;
    public static boolean debugEnabled = false;

    private void initialize() {
        GameManager.loadServerProfile();

        // Listeners
        addListener(new RPlayerListener());
        addListener(new ChatHandler());
        addListener(new RPlayerJoinListener());
        addListener(new RPlayerQuitListener());
        addListener(new BlockedCmds());
        addListener(new ServerUpdateListener());
        addListener(new WeatherChangeListener());

        // Admin Commands
        addCommand(new BroadcastAdminCmd());
        addCommand(new Giveaway());
        addCommand(new OpCmd());
        addCommand(new RankCmd());
        addCommand(new RestartCmd());
        addCommand(new ErrorFixedCmd());
        addCommand(new TweetCmd());
        addCommand(new PollCmd());

        // Mod Commands
        addCommand(new GameModeCmd());
        addCommand(new SayCmd());
        addCommand(new SetWhitelistCmd());
        addCommand(new SilenceChatCmd());
        addCommand(new StaffChatCmd());
        addCommand(new STFUCmd());
        addCommand(new TeleportBringCmd());
        addCommand(new TeleportCmd());
        addCommand(new BanCmd());
        addCommand(new TempBanCmd());
        addCommand(new MuteCmd());
        addCommand(new TempMuteCmd());
        addCommand(new KickCmd());
        addCommand(new WarnCmd());
        addCommand(new UnbanCmd());
        addCommand(new UnmuteCmd());
        addCommand(new LookupCmd());

        // VIP Commands
        addCommand(new GoToCmd());

        //Premium Commands
        addCommand(new ColorCmd());

        // Game Commands
        addCommand(new CreditsCommand());
        addCommand(new PingCmd());
        addCommand(new RPICoreInfoCommand());
        addCommand(new TeamCommand());
        addCommand(new WhereAmICommand());
        addCommand(new UngroundCmd());
        addCommand(new ReplyMessageCmd());
        addCommand(new HelpCmd());


        // Server Commands
        addCommand(new HubCmd());
        addCommand(new MessageCmd());
        addCommand(new ReportCmd());
        addCommand(new ListCmd());
        addCommand(new RulesCmd());
        addCommand(new ShopCmd());
        addCommand(new FlyCmd());
        addCommand(new FriendCmd());
        addCommand(new StatsCmd());
        addCommand(new TogglesCmd());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Messages());

        try {
            MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `accounts` (" +
                    "  `uuid` VARCHAR(100) NOT NULL," +
                    "  `username` VARCHAR(16) NOT NULL," +
                    "  `rpid` INT NOT NULL  AUTO_INCREMENT," +
                    "  `rank` VARCHAR(12) NULL," +
                    "  `credits` INT NOT NULL," +
                    "  `ip` VARCHAR(20) NULL," +
                    "  PRIMARY KEY (`rpid`))").executeUpdate();

            MySQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `online_players` (" +
                    " `id` INT NOT NULL," +
                    " `username` VARCHAR(16) NOT NULL," +
                    " `server` VARCHAR(100) NOT NULL)").executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ShopManager.getInstance().loadGlobalItems();
        PunishmentManager.getInstance().checkDatabase();

        checkDownloadables();

        if(debugEnabled) System.out.println("RPICore Initialize successful");
    }

    private void checkDownloadables() {
        new BukkitRunnable() { //task because it waits till the server is done starting
            public void run() {
                LibLoader.loadLib("twitter4j-core-4.0.3.jar", "http://gwnetworking.x10host.com/company/files/twitter4j-core-4.0.3.jar");

                //PluginUpdateChecker.checkForUpdate("RPI", "https://www.dropbox.com/s/fyuoh6szwl0ykef/RPI.jar?dl=1");
                //PluginUpdateChecker.checkForUpdate(GameManager.getGame().getName(), GameManager.getGame().getDownload());
            }
        }.runTask(getInstance());
    }

    @Override
    public void onEnable() {
        instance = this;
        initialize();
    }

    @Override
    public void onDisable() {
        new Thread() {
            public void run() {
                for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
                    pl.saveData();
                    PlayerManager.getInstance().removePlayer(pl.getPlayer());
                }
            }
        }.start();

        EntityRegistration.clearAll();
        TeamManager.unregisterAllTeams();

        instance = null;
    }

    public void addCommand(RCommand cmd) {
        try {
            RCommandMap.register(cmd);
        } catch (Exception ignored) {}
    }

    public void addListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public static RPICore getInstance() {
        return instance;
    }

    public static Plugin getPlugin() {
        return instance;
    }

}
