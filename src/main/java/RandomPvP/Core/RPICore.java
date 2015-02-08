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
import RandomPvP.Core.Punish.PunishmentManager;
import RandomPvP.Core.Util.Broadcasts;
import RandomPvP.Core.Util.Tab.HidePlayerList;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
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

    public static boolean pEnabled = false;

    public static boolean debugEnabled = true;

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

        // VIP Commands
        addCommand(new GoToCmd());

        // Game Commands
        addCommand(new CreditsCommand());
        addCommand(new PingCmd());
        addCommand(new RPICoreInfoCommand());
        addCommand(new TeamCommand());
        addCommand(new WhereAmICommand());

        // Server Commands
        addCommand(new HubCmd());
        addCommand(new MessageCmd());
        addCommand(new ReportCmd());

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new Broadcasts());

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

    }


    @Override
    public void onEnable() {
        instance = this;
        serverStart = System.currentTimeMillis();
        initialize();
    }

    @Override
    public void onDisable() {

        for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            pl.getPlayer().kickPlayer("§4Server is restarting!");
        }

        list.cleanupAll();

        try {
            GameManager.unloadGame();
        } catch (SQLException e) {
            e.printStackTrace();
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
}
