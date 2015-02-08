package RandomPvP.Core.Event.Server;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

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
public class RankWhitelistChangeEvent extends Event implements Cancellable {

    private Rank rank;
    private RPlayer player;
    private boolean cancelled = false;
    private static HandlerList handlers = new HandlerList();

    public RankWhitelistChangeEvent(Object pl, Rank to) {
        rank = to;
        if (pl instanceof RPlayer) {
            player = (RPlayer) pl;
        }

        Bukkit.broadcastMessage("§6§l>> " + (pl instanceof RPlayer ? ((RPlayer) pl).getRankedName(false) : "§4Console")  + " §eset the minimum rank to " + rank.getName() + "§e.");

        if (PlayerManager.getInstance().getOnlinePlayers() != null) {
            if (PlayerManager.getInstance().getOnlinePlayers().size() > 0) {
                for (RPlayer po : PlayerManager.getInstance().getOnlinePlayers()) {
                    if (!po.getRank().has(to)) {
                        po.getPlayer().kickPlayer("You must have " + to.getTag() + "§fto join this server!");
                    }
                }
            }
        }
    }

    public void setPlayer(RPlayer pl) {
        this.player = pl;
    }

    public RPlayer getPlayer() {
        return player;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public String getName() {
        return rank.getName();
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

    public static HandlerList getHandlerList() { return handlers; }
}
