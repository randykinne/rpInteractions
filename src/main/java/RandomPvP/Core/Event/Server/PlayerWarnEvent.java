package RandomPvP.Core.Event.Server;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Punish.Punishment;
import RandomPvP.Core.Util.Broadcasts;
import RandomPvP.Core.Util.NumberUtil;
import RandomPvP.Core.Util.Player.UUID.UUIDUtil;
import org.bukkit.Bukkit;
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
public class PlayerWarnEvent extends Event {

    private Punishment punishment;
    private static HandlerList handlers = new HandlerList();

    public PlayerWarnEvent(Punishment pm) {
        Broadcasts.sendRankedBroadcast(Rank.MOD, false, true, new OfflineRPlayer(UUIDUtil.getName(pm.getIssuer())).getRankedName(false) +
                " §5" + pm.getType().getVerb().toLowerCase() + NumberUtil.translateDuration(pm.getEnd()) + " §5for §7§n" + pm.getReason() + " §5on " +
                GameManager.getGame().getPrimaryColor() + "§l" + Bukkit.getServerName() + "§5.");
    }

    public Punishment getPunishment() {
        return punishment;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() { return handlers; }
}