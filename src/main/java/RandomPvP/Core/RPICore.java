package RandomPvP.Core;

import RandomPvP.Core.Chat.ChatHandler;
import RandomPvP.Core.Commands.Admin.*;
import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Commands.Game.*;
import RandomPvP.Core.Commands.Mod.*;
import RandomPvP.Core.Commands.Command.RCommandMap;
import RandomPvP.Core.Commands.Server.*;
import RandomPvP.Core.Commands.VIP.GoToCmd;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Listener.*;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.Shop.Items.BoosterItem;
import RandomPvP.Core.Util.Shop.ShopManager;
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Util.Broadcasts;
import RandomPvP.Core.Util.Tab.HidePlayerList;
import RandomPvP.Core.Util.Vote.Votifier;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
public class RPICore extends JavaPlugin {

    public static boolean pEnabled = true;

    public static boolean debugEnabled = false;

    public static long serverStart;

    private int nextId = 0;

    public static HidePlayerList list;

    void initialize() {

        list = new HidePlayerList(this);

        // Listeners
        addListener(new RPlayerListener());
        addListener(new ChatHandler());
        addListener(new RPlayerJoinListener());
        addListener(new RPlayerQuitListener());
        addListener(new BlockedCmds());
        addListener(new ServerUpdateListener());
        addListener(new VoteEvent());

        // Admin Commands
        addCommand(new BroadcastAdminCmd());
        addCommand(new Giveaway());
        addCommand(new OpCmd());
        addCommand(new RankAdminCmd());
        addCommand(new TestCommand());

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

        // Game Commands
        addCommand(new CreditsCommand());
        addCommand(new PingCmd());
        addCommand(new RPICoreInfoCommand());
        addCommand(new TeamCommand());
        addCommand(new WhereAmICommand());
        addCommand(new UngroundCmd());

        // Server Commands
        addCommand(new HubCmd());
        addCommand(new MessageCmd());
        addCommand(new ReportCmd());
        addCommand(new ListCmd());
        addCommand(new RulesCmd());
        addCommand(new ShopCmd());
        addCommand(new PollCmd());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Broadcasts());

        getConfig().addDefault("voteEnabled", false);
        getConfig().options().copyDefaults(true);
        saveConfig();

        String sqlCreate =
                "CREATE TABLE IF NOT EXISTS `accounts` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `username` VARCHAR(16) NOT NULL," +
                        "  `rpid` INT NOT NULL  AUTO_INCREMENT," +
                        "  `rank` VARCHAR(12) NULL," +
                        "  `rank_updated` BIGINT(8) NULL," +
                        "  `credits` INT NOT NULL," +
                        "  `ip` VARCHAR(20) NULL," +
                        "  `email` VARCHAR(100) NULL," +
                        "  PRIMARY KEY (`rpid`))";

        String sqlCreate2 = "CREATE TABLE IF NOT EXISTS `online_players` (" +
                " `id` INT NOT NULL," +
                " `username` VARCHAR(16) NOT NULL," +
                " `server` VARCHAR(100) NOT NULL)";

        String sqlCreate3 = "CREATE TABLE IF NOT EXISTS `credit_booster` (" +
                " `id` INT NOT NULL AUTO_INCREMENT," +
                " `rpid` INT NOT NULL," +
                " `duration` BIGINT(8) NOT NULL," +
                " `multiplier` INT NOT NULL," +
                " PRIMARY KEY (`id`))";

        String sqlGetNextId = "SELECT MAX(rpid) AS max FROM accounts";

        try {
            PreparedStatement statement = MySQL.getConnection().prepareStatement(sqlCreate);
            statement.executeUpdate();

            statement = MySQL.getConnection().prepareStatement(sqlCreate2);
            statement.executeUpdate();

            statement = MySQL.getConnection().prepareStatement(sqlCreate3);
            statement.executeUpdate();

            statement = MySQL.getConnection().prepareStatement(sqlGetNextId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                nextId = rs.getInt("max") + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pEnabled) PunishmentManager.getInstance().checkDatabase();

        if (debugEnabled) {
            System.out.println("RPICore Initialize successful");
        }

        if(voteEnabled()) {
            getVote();
        }

        ShopManager.getInstance().addItem(new BoosterItem());

        GameManager.loadServerProfile();
    }


    @Override
    public void onEnable() {
        instance = this;
        serverStart = System.currentTimeMillis();
        initialize();
    }

    @Override
    public void onDisable() {
        //sleeping to remove it from the DB
        Thread t = new Thread() {
            public void run() {
                try {
                    {
                        for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
                            PreparedStatement stmt = MySQL.getConnection().prepareStatement("DELETE FROM `online_players` WHERE `id`=?");
                            {
                                stmt.setInt(1, pl.getRPID());
                            }
                            stmt.executeUpdate();
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        };
        t.start();
        try{Thread.sleep(1000);}catch(Exception ignored){}

        list.cleanupAll();

        if(voteEnabled()) {
            getVote().onDisable();
        }

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

    private static RPICore instance;

    public static RPICore getInstance() {
        return instance;
    }

    public static Plugin getPlugin() { return instance; }

    private static Votifier v;

    public Votifier getVote() {
        if(voteEnabled()) {
            if(v == null) {
                v = new Votifier(getInstance());
                v.onEnable();
            }
        }
        return v;
    }

    public static boolean voteEnabled() {
        return getInstance().getConfig().getBoolean("voteEnabled");
    }

}
