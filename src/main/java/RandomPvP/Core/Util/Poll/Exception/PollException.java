package RandomPvP.Core.Util.Poll.Exception;

import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class PollException extends Exception {

    public PollException(RPlayer p) {
        this(p, "No reason given.");
    }

    public PollException(RPlayer p, String reason) {
        p.message(MsgType.ERROR, reason);
    }

}
