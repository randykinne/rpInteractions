package RandomPvP.Core.Server.General.Friends;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class FriendManager {

    private static FriendManager manager = new FriendManager();



    public static FriendManager getInstance() {
        return manager;
    }
}
