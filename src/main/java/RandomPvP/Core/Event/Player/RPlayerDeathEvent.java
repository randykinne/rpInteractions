package RandomPvP.Core.Event.Player;

import RandomPvP.Core.Player.RPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

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
public class RPlayerDeathEvent extends Event implements Cancellable {

    RPlayer killed;
    RPlayer killer;
    EntityDamageEvent.DamageCause cause;
    String message;

    private boolean cancelled = false;
    private static final HandlerList handlers = new HandlerList();

    public RPlayerDeathEvent(RPlayer killee, RPlayer killer, EntityDamageEvent.DamageCause cause, String message) {
        killed = killee;
        this.killer = killer;
        this.cause = cause;
    }

    public RPlayer getPlayer() {
        return killed;
    }

    public void setPlayer(RPlayer pl) {
        killed = pl;
    }

    public RPlayer getKiller() {
        return killer != null ? killer : getPlayer().getPlayerLastHitBy();
    }

    public void setKiller(RPlayer killer) {
        this.killer = killer;
    }

    public EntityDamageEvent.DamageCause getCause() {
        return cause;
    }

    public void setCause(EntityDamageEvent.DamageCause c) {
        cause = c;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        msg = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
