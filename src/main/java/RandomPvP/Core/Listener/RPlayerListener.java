package RandomPvP.Core.Listener;

import RandomPvP.Core.Event.Player.RPlayerDeathEvent;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Server.Game.Team.Team;
import RandomPvP.Core.Util.Entity.EntityUtils;
import RandomPvP.Core.Util.NumberUtil;
import net.minecraft.server.v1_7_R4.EnumClientCommand;
import net.minecraft.server.v1_7_R4.PacketPlayInClientCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Random;

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
public class RPlayerListener implements Listener {

    private HashMap<RPlayer, Action> last = new HashMap<>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        if(!e.isCancelled()) {
            RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
            if (pl.getTeam() != null) {
                if (pl.getGameMode() != GameMode.CREATIVE) {
                    if (pl.getTeam().getType() == Team.Type.Observing) {
                        if(e.getAction() == Action.PHYSICAL && last.get(pl) == Action.PHYSICAL) return;
                        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_AIR) {
                            pl.message(MsgType.ERROR, "You cannot interact.");
                        }
                        last.put(pl, e.getAction());
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnter(VehicleEnterEvent e) {
        if(!e.isCancelled()) {
            if (e.getEntered() instanceof Player) {
                RPlayer pl = PlayerManager.getInstance().getPlayer((Player) e.getEntered());
                if(pl.getTeam() != null) {
                    if(pl.getTeam().getType() == Team.Type.Observing) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        if(!e.isCancelled()) {
            if (e.getEntity() instanceof Player) {
                RPlayer pl = PlayerManager.getInstance().getPlayer((Player) e.getEntity());

                if (pl.getTeam() != null) {
                    if (pl.getTeam().getType() == Team.Type.Observing) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if(!e.isCancelled()) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                RPlayer damager = PlayerManager.getInstance().getPlayer((Player) e.getDamager());

                if (damager.getTeam() != null) {
                    if (damager.getTeam().getType() == Team.Type.Observing) {
                        damager.message(MsgType.ERROR, "You cannot interact.");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        new BukkitRunnable() {
            public void run() {
                ((CraftPlayer) e.getEntity()).getHandle().playerConnection.a(new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN));
            }
        }.runTaskLater(RPICore.getInstance(), 1L);

        Player p = e.getEntity();
        RPlayer pl = PlayerManager.getInstance().getPlayer(p);
        Player killer = null;

        EntityDamageEvent entityEvent = p.getLastDamageCause();

        Random rand = new Random();

        if(pl.getPlayer() != null) {

            if (pl.getPlayerLastHitBy() != null && pl.getPlayerLastHitBy().getPlayer() != null) {
                killer = pl.getPlayerLastHitBy().getPlayer();
            }

            if(entityEvent.getCause() != null) {
                if (entityEvent instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) entityEvent;

                    if (damageEvent.getDamager() instanceof Player) {
                        killer = p.getKiller();

                        switch (rand.nextInt(5) + 1) {
                            case 1:
                                e.setDeathMessage("§8§l>> " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + " §7stabbed " +
                                        pl.getRankedName(false) + " §7to death!");
                                break;
                            case 2:
                                e.setDeathMessage("§8§l>> " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + " §7slaughtered " +
                                        pl.getRankedName(false) + "§7!");
                                break;
                            case 3:
                                e.setDeathMessage("§8§l>> " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + " §7killed " +
                                        pl.getRankedName(false) + "§7!");
                                break;
                            case 4:
                                e.setDeathMessage("§8§l>> " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + " §7obliterated " +
                                        pl.getRankedName(false) + "§7!");
                                break;
                            case 5:
                                e.setDeathMessage("§8§l>> " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + " §7murdered " +
                                        pl.getRankedName(false) + "§7!");
                                break;
                        }

                    } else if (damageEvent.getDamager() instanceof TNTPrimed) {
                        e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + (pl.getPlayerLastHitBy() != null ? " §7was knocked into TNT by " + pl.getPlayerLastHitBy().getRankedName(false) + "§7." : " §7was blown up by TNT!"));
                    } else if (damageEvent.getDamager() instanceof Projectile) {
                        Projectile projectile = (Projectile) damageEvent.getDamager();
                        if (projectile.getShooter() instanceof Player) {
                            killer = (Player) projectile.getShooter();
                            e.setDeathMessage("§8§l>> " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + " §7shot " +
                                    pl.getRankedName(false) + "§7! §b(" + NumberUtil.getDistanceBetween(p.getLocation(), killer.getLocation()) + " blocks)");
                        }
                    } else if (EntityUtils.isCreature(damageEvent.getDamager())) {
                        if (damageEvent.getDamager() instanceof LivingEntity) {
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7was mobbed by a mob!");
                        }

                    } else if (damageEvent.getDamager() instanceof FallingBlock) {
                        e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7was rekt by a falling anvil!");
                    } else {
                        e.setDeathMessage(null);
                    }
                } else if (entityEvent.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7drowned!");
                } else if (entityEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    switch (rand.nextInt(3) + 1) {
                        case 1:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7learned that what goes up must come down! §e(" + new DecimalFormat("##.#").format(entityEvent.getEntity().getFallDistance()) + " blocks!)");
                            break;
                        case 2:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7fell to their death! §e(" + new DecimalFormat("##.#").format(entityEvent.getEntity().getFallDistance()) + " blocks!)");
                            break;
                        case 3:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7tried to fly! §e(" + new DecimalFormat("##.#").format(entityEvent.getEntity().getFallDistance()) + " blocks!)");
                            break;
                        case 4:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7attempted to become a bird! §e(" + new DecimalFormat("##.#").format(entityEvent.getEntity().getFallDistance()) + " blocks!)");
                            break;
                    }
                } else if (entityEvent.getCause() == EntityDamageEvent.DamageCause.FIRE || entityEvent.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    if (entityEvent.getEntity().getLastDamageCause() instanceof Player) {
                        killer = (Player) entityEvent.getEntity().getLastDamageCause();
                        switch (rand.nextInt(3) + 1) {
                            case 1:
                                e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7burnt to a crisp while fighting " + PlayerManager.getInstance().getPlayer(killer).getRankedName(false) + "!");
                                break;
                            case 2:
                                e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7stepped into fire!");
                                break;
                            case 3:
                                e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7melted!");
                                break;
                        }
                    }
                    switch (rand.nextInt(3) + 1) {
                        case 1:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7burnt to a crisp!");
                            break;
                        case 2:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7stepped into fire!");
                            break;
                        case 3:
                            e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7melted!");
                            break;
                    }
                } else if (entityEvent.getCause() == EntityDamageEvent.DamageCause.LIGHTNING) {
                    e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7was struck by lightning!");
                } else if (entityEvent.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    e.setDeathMessage("§8§l>> " + pl.getRankedName(false) + " §7fell out of the world!");
                } else {
                    e.setDeathMessage(null);
                }
            } else {
                e.setDeathMessage(null);
            }

            pl.getStats().addDeath();
            pl.saveStats();

            if(killer != null) {
                PlayerManager.getInstance().getPlayer(killer).getStats().addKill();
                PlayerManager.getInstance().getPlayer(killer).saveStats();
            }

            Bukkit.getPluginManager().callEvent(new RPlayerDeathEvent(pl, killer != null ? PlayerManager.getInstance().getPlayer(killer) : null, entityEvent.getCause(), e.getDeathMessage()));
        }
    }
}
