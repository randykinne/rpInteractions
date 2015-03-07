package RandomPvP.Core.Listener;

import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.RPICore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class VoteEvent implements Listener {

    @EventHandler
    public void onVote(RandomPvP.Core.Util.Vote.model.VoteEvent e) {
        System.out.println("GOT VOTE!");
        if(RPICore.voteEnabled()) {
            System.out.println("VOTE ENABLED!");
            OfflineRPlayer pl = new OfflineRPlayer(e.getVote().getUsername());
            pl.addCredits(70);
            pl.saveData();
            System.out.println("ADDED COINS!");
        }
    }

}
