package RandomPvP.Core;

import RandomPvP.Core.Chat.ChatHandler;
import RandomPvP.Core.Commands.Admin.BroadcastAdminCmd;
import RandomPvP.Core.Commands.Admin.OpCmd;
import RandomPvP.Core.Commands.Admin.RankAdminCmd;
import RandomPvP.Core.Commands.Game.CreditsCommand;
import RandomPvP.Core.Commands.Game.RPICoreInfoCommand;
import RandomPvP.Core.Commands.Game.TeamCommand;
import RandomPvP.Core.Commands.Game.WhereAmICommand;
import RandomPvP.Core.Commands.Mod.*;
import RandomPvP.Core.Commands.Server.*;
import RandomPvP.Core.Commands.VIP.GoToCmd;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Listener.DeathListener;
import RandomPvP.Core.Listener.JoinListener;
import RandomPvP.Core.Listener.QuitListener;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Player.UUIDCache;
import RandomPvP.Core.Punishment.Configuration;
import RandomPvP.Core.Punishment.Database;
import RandomPvP.Core.Punishment.PunishmentManager;
import RandomPvP.Core.Punishment.command.CommandHandler;
import RandomPvP.Core.Util.RPStaff;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


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

    public static long serverStart;

    private CommandsManager<CommandSender> commands;

    private Configuration config;
    private Database database;
    private PunishmentManager manager;
    private CommandHandler handler;
    private LookupCmd cmd;
    private int nextId = 1;

    public Database getPunishmentDatabase() { return database; }
    public PunishmentManager getPunishmentManager() {
        return manager;
    }

    void initialize() {

        config = new Configuration(this);
        database = new Database(this, config);
        database.connect();
        manager = new PunishmentManager(this, config, database);
        handler = new CommandHandler(manager);
        cmd = new LookupCmd(manager);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new JoinListener(manager), this);
        pm.registerEvents(new ChatHandler(manager), this);
        pm.registerEvents(new DeathListener(), this);
        pm.registerEvents(new ModPanelCmd(), this);
        pm.registerEvents(new QuitListener(), this);

        setupCommands();
        getCommand("kick").setExecutor(handler);
        getCommand("ban").setExecutor(handler);
        getCommand("unban").setExecutor(handler);
        getCommand("mute").setExecutor(handler);
        getCommand("unmute").setExecutor(handler);

        RPStaff.nagStaffMembers();

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "Chat");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "Chat", new RPStaff());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "VIP");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "VIP", new RPStaff());
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "ABroadcast");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "ABroadcast", new RPStaff());

        String sqlCreate =
                "CREATE TABLE IF NOT EXISTS `accounts` (" +
                        "  `uuid` VARCHAR(100) NOT NULL," +
                        "  `username` VARCHAR(16) NOT NULL," +
                        "  `rpid` INT NOT NULL  AUTO_INCREMENT," +
                        "  `rank` VARCHAR(12) NULL," +
                        "  `credits` INT NOT NULL," +
                        "  `ip` VARCHAR(20) NULL," +
                        "  `email` VARCHAR(100) NULL," +
                        "  PRIMARY KEY (`rpid`))";

        String sqlGetNextId = "SELECT MAX(rpid) AS max FROM accounts";

        try {
            PreparedStatement statement = MySQL.getConnection().prepareStatement(sqlCreate);
            statement.executeUpdate();

            statement = MySQL.getConnection().prepareStatement(sqlGetNextId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                nextId = rs.getInt("max") + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        UUIDCache.startFetch(instance);
        /*String createLog = "CREATE TABLE IF NOT EXISTS `chatlog` (`id` INT(10) NOT NULL AUTO_INCREMENT, `uuid` VARCHAR(40) NOT NULL, `message` TEXT NOT NULL, `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, `caseid` INT(10) NOT NULL DEFAULT '0', PRIMARY KEY (`id`)) ENGINE = MyISAM;";
        try {
            PreparedStatement stmt2 = MySQL.getConnection().prepareStatement(createLog);
            stmt2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };

        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        //Register your commands here
        cmdRegister.register(BroadcastAdminCmd.class); // ADMIN
        cmdRegister.register(CreditsCommand.class); //GAME
        cmdRegister.register(GamemodeCmd.class); // MOD-BUILDER
        cmdRegister.register(GoToCmd.class); //VIP
        cmdRegister.register(HubCmd.class); // SERVER
        cmdRegister.register(MessageCmd.class); // SERVER
        cmdRegister.register(ModPanelCmd.class); //MOD
        cmdRegister.register(LookupCmd.class); //MOD
        cmdRegister.register(OpCmd.class); //ADMIN
        cmdRegister.register(RankAdminCmd.class); // ADMIN
        cmdRegister.register(RDPVPCmd.class); // SERVER
        cmdRegister.register(ReportCmd.class); // SERVER
        cmdRegister.register(RPICoreInfoCommand.class); // GAME
        cmdRegister.register(SayCmd.class); // MOD
        cmdRegister.register(SetWhitelistCmd.class); //MOD
        cmdRegister.register(SilenceChatCmd.class); // MOD
        cmdRegister.register(STFUCmd.class); // MOD
        cmdRegister.register(TeamCommand.class); // GAME
        cmdRegister.register(TeleportCmd.class); // MOD
        cmdRegister.register(WarningCmd.class); // MOD
        cmdRegister.register(WhereAmICommand.class); // GAME


    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + ">> " + ChatColor.GRAY +  e.getMessage());
        }

        return true;
    }

    @Override
    public void onEnable() {
        instance = this;
        serverStart = System.currentTimeMillis();
        initialize();

    }

    @Override
    public void onDisable() {
        for (int i = 0; i<RPlayerManager.getInstance().getOnlinePlayers().size(); i++) {
            RPlayerManager.getInstance().getOnlinePlayers().remove(i);
        }

        manager.forceUpdate();
        database.disconnect();
        manager.stop();

        instance = null;

    }

    private static RPICore instance;
    public static RPICore getInstance() { return instance; }
}
