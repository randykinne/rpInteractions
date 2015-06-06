package RandomPvP.Core.Player.Friend;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Util.NetworkUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public abstract class Friend {

    private List<Friend> friends = new ArrayList<>();
    private List<Friend> requests = new ArrayList<>();

    public abstract String getPlayer();

    public List<Friend> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    public List<Friend> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    public void removeFriend(Friend friend) {
        friends.remove(friend);
    }

    public void addRequest(Friend friend) {
        requests.add(friend);
    }

    public void requestFriendship(Friend friend) {
        friend.addRequest(friend);
    }

    public void acceptRequest(Friend friend) {
        requests.remove(friend);
        friends.add(friend);
    }

    public void save() {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("");
                    {

                    }
                    ResultSet res = stmt.executeQuery();
                    if(res.next()) {

                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

}
