package RandomPvP.Core.AntiCheat.KillAura;

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
import java.util.*;
import java.util.logging.Level;

import RandomPvP.Core.AntiCheat.KillAura.Packet.WrapperPlayClientUseEntity;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.RPlayerManager;
import RandomPvP.Core.Punishment.PunishmentManager;
import RandomPvP.Core.Punishment.Punishments;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Util.RPStaff;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class AntiAura implements Listener, CommandExecutor {

    private HashMap<UUID, AuraCheck> running = new HashMap<>();
    private HashMap<UUID, Long> lastHit = new HashMap<>();
    private boolean notifyPermission = RPICore.getInstance().notifyPermission;
    private boolean randomCheckOnFight = RPICore.getInstance().randomCheckOnFight;
    private boolean worldPvpCheck = RPICore.getInstance().worldPvpCheck;
    private boolean iCanHasPVP = RPICore.getInstance().iCanHasPVP;
    private boolean isRegistered = RPICore.getInstance().isRegistered;
    private boolean customCommandToggle = RPICore.getInstance().customCommandToggle;
    private boolean visOrInvisible = RPICore.getInstance().visOrInvisible;
    private boolean visCmd = RPICore.getInstance().visCmd;
    private static boolean silentBan = RPICore.getInstance().silentBan;
    private String typeCmd = RPICore.getInstance().typeCmd;
    private String type = RPICore.getInstance().type;
    private String customCommand = RPICore.getInstance().customCommand;
    private static String banMessage = RPICore.getInstance().banMessage;
    private static String kickMessage = RPICore.getInstance().kickMessage;
    private int minToAutoRun = RPICore.getInstance().minToAutoRun;
    private int count = RPICore.getInstance().count;
    private int maxToCheck = RPICore.getInstance().maxToCheck;
    private int fightTimeDelay = RPICore.getInstance().fightTimeDelay;
    private static int runEvery = RPICore.getInstance().runEvery;
    public static int total = RPICore.getInstance().total;
    private static int autoBanCount = RPICore.getInstance().autoBanCount;
    private long fightTimeDelayTrue = RPICore.getInstance().fightTimeDelayTrue;
    public static final Random RANDOM = new Random();


    public Player getRandomPlayer() {
        ArrayList<RPlayer> players = new ArrayList<>();

        for (RPlayer player : RPlayerManager.getInstance().getOnlinePlayers()) {
            players.add(player);
        }

        return players.get(RANDOM.nextInt(players.size())).getPlayer();
    }

    public void findPlayerWorld() {
        while(!iCanHasPVP) {
            if(count > maxToCheck) {
                count = 0;
                break;
            }
            Player player = getRandomPlayer();
            count++;
            if(player.getWorld().getPVP()) {
                iCanHasPVP = true;
                checkExecute(player.getName());
            }
        }
        iCanHasPVP = false;
    }

    public void checkExecute(String player) {
        org.bukkit.Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "auracheck " + player);
    }

    public void register() {
        ProtocolLibrary.getProtocolManager().addPacketListener(
                new PacketAdapter(RPICore.getInstance(), WrapperPlayClientUseEntity.TYPE) {
                    @Override
                    public void onPacketReceiving(PacketEvent event) {
                        if (event.getPacketType() == WrapperPlayClientUseEntity.TYPE) {
                            int entID = new WrapperPlayClientUseEntity(event.getPacket()).getTargetID();
                            if (running.containsKey(event.getPlayer().getUniqueId())) {
                                running.get(event.getPlayer().getUniqueId()).markAsKilled(entID);
                            }
                        }
                    }

                });
        this.isRegistered = true;
    }

    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListeners(RPICore.getInstance());
        this.isRegistered = false;
    }

    public AuraCheck remove(UUID id) {
        if (this.running.containsKey(id)) {

            if (running.size() == 1) {
                this.unregister();
            }

            return this.running.remove(id);
        }
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage("§4§l>> §7Player is not online.");
            return true;
        }

        if (!isRegistered) {
            this.register();
        }

        if(args.length >= 2) {
            if(args[1].equalsIgnoreCase("standing") || args[1].equalsIgnoreCase("running")) {
                typeCmd = args[1];
            } else {
                typeCmd = type;
            }
        } else {
            typeCmd = type;
        }

        if(args.length >= 3) {
            if(args[2].equalsIgnoreCase("visible") || args[2].equalsIgnoreCase("invisible")) {
                if(args[2].equalsIgnoreCase("visible")) {
                    visCmd = false;
                } else {
                    visCmd = true;
                }
            } else {
                visCmd = visOrInvisible;
            }
        } else {
            visCmd = visOrInvisible;
        }

        AuraCheck check = new AuraCheck(this, player);
        running.put(player.getUniqueId(), check);

        check.invoke(sender, typeCmd, visCmd, new AuraCheck.Callback() {
            @Override
            public void done(long started, long finished, AbstractMap.SimpleEntry<Integer, Integer> result, CommandSender invoker, Player player) {
                if (invoker instanceof Player && !((Player) invoker).isOnline()) {
                    return;
                }

                invoker.sendMessage("§6§l>> " + ChatColor.YELLOW + "Aura check on " + player.getName() + " result: killed " + result.getKey() + " out of " + result.getValue());
                double timeTaken = finished != Long.MAX_VALUE ? (int) ((finished - started) / 1000) : ((double) RPICore.getInstance().getConfig().getInt("settings.ticksToKill", 10) / 20);
                invoker.sendMessage("§6§l>> " + ChatColor.YELLOW + "Check length: " + timeTaken + " seconds.");
                if(result.getKey() >= autoBanCount) {
                    if(notifyPermission) {
                        RPStaff.sendStaffMessage("(AutoModerator) Banning player " + player.getName() + "for going beyond AntiAura threshold.", false);
                    }

                    Bukkit.getLogger().log(Level.INFO, "Banning player {0} for going beyond AntiAura threshold.", player.getName());
                    if(!customCommandToggle) {
                        RPICore.getInstance().getPunishmentManager().addPunishment(PunishmentManager.PunishmentType.BAN, player.getUniqueId(), null, System.currentTimeMillis(), RPICore.getInstance().getPunishmentManager().PUNISHMENT_EXPIRE_NEVER, Bukkit.getServerName(), "Cheating (Hacked Client/KillAura)");
                    } else {
                        String disposableCommand = customCommand;
                        if(customCommand.contains("%player")) {
                            disposableCommand = disposableCommand.replace("%player", player.getName());
                        }
                        if(customCommand.contains("%count")) {
                            disposableCommand = disposableCommand.replace("%count", result.getKey() + "");
                        }

                        org.bukkit.Bukkit.dispatchCommand(Bukkit.getConsoleSender(), disposableCommand);
                    }
                    if(!silentBan && !customCommandToggle) {
                        player.kickPlayer(Punishments.formatKickMessage(kickMessage));
                    }
                }
            }
        });
        return true;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if(randomCheckOnFight) {
            if(event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER) {
                if(lastHit.containsKey(event.getDamager().getUniqueId())) {
                    if((lastHit.get(event.getDamager().getUniqueId()) + fightTimeDelayTrue) < System.currentTimeMillis()) {
                        final String name = ((Player)event.getDamager()).getName();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                checkExecute(name);
                            }
                        }.runTaskLater(RPICore.getInstance(), 60L); // 3 seconds after fight starts
                    }
                } else {
                    lastHit.put(event.getDamager().getUniqueId(), System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        AuraCheck check = this.remove(event.getPlayer().getUniqueId());
        if (check != null) {
            check.end();
        }

        if(lastHit.containsKey(event.getPlayer().getUniqueId())) {
            lastHit.remove(event.getPlayer().getUniqueId());
        }
    }
}

