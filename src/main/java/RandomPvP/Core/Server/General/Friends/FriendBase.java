package RandomPvP.Core.Server.General.Friends;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.OfflineRPlayer;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.StringUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class FriendBase {

    private void addToDatabase(final OfflineRPlayer p) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `friends` WHERE `rpid` = ?");
                    {
                        stmt.setInt(1, p.getRPID());
                    }

                    ResultSet res = stmt.executeQuery();
                    if (!(res.next())) {
                        PreparedStatement stmt1 = MySQL.getConnection().prepareStatement("INSERT INTO `friends` (`rpid`, `friends`, `requested`) VALUES (?,?,?)");
                        {
                            stmt1.setInt(1, p.getRPID());
                            stmt1.setString(2, "");
                            stmt1.setString(3, "");
                        }
                        stmt1.executeUpdate();
                    }
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void addFriend(final OfflineRPlayer p, final String friend) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `friends` SET `friends` = ? WHERE `rpid` = ?");
                    {
                        stmt.setString(1, getFriends(p) + new OfflineRPlayer(friend).getRPID() + "|");
                        stmt.setInt(2, p.getRPID());
                    }

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void removeFriend(final OfflineRPlayer p, final String friend) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `friends` SET `friends` = ? WHERE `rpid` = ?");
                    {
                        stmt.setString(1, getFriends(p).replace(new OfflineRPlayer(friend).getRPID() + "|", ""));
                        stmt.setInt(2, p.getRPID());
                    }

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public String getFriends(final OfflineRPlayer p) {
        try {
            ExecutorService s = Executors.newCachedThreadPool();
            Future<ResultSet> task = s.submit(new Callable<ResultSet>() {
                @Override
                public ResultSet call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `friends` FROM `friends` WHERE `rpid` = ?");
                    {
                        stmt.setInt(1, p.getRPID());
                    }
                    return stmt.executeQuery();
                }
            });
            ResultSet res = task.get();
            if(res.next()) {
                return res.getString("friends");
            } else {
                addToDatabase(p);
                return null;
            }
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

    public int[] getFriendsArray(final OfflineRPlayer p) {
        try {
            if(getFriends(p) != null) {
                return StringUtil.stringToIntArray(getFriends(p).split("\\|"));
            } else {
                addToDatabase(p);
                return null;
            }
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

    public void addWaitingRequest(final OfflineRPlayer p, final String requested) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `friends` SET `requested` = ? WHERE `rpid` = ?");

                    {
                        stmt.setString(1, getWaitingRequests(p) + new OfflineRPlayer(requested).getRPID() + "|");
                        stmt.setInt(2, p.getRPID());
                    }

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void removeWaitingRequest(final OfflineRPlayer p, final String requested) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("UPDATE `friends` SET `requested` = ? WHERE `rpid` = ?");

                    {
                        stmt.setString(1, getWaitingRequests(p).replace(new OfflineRPlayer(requested).getRPID() + "|", ""));
                        stmt.setInt(2, p.getRPID());
                    }

                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public String getWaitingRequests(final OfflineRPlayer p) {
        try {
            ExecutorService s = Executors.newCachedThreadPool();
            Future<ResultSet> task = s.submit(new Callable<ResultSet>() {
                @Override
                public ResultSet call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `requested` FROM `friends` WHERE `rpid` = ?");
                    {
                        stmt.setInt(1, p.getRPID());
                    }
                    return stmt.executeQuery();
                }
            });
            ResultSet res = task.get();
            if(res.next()) {
                return res.getString("requested");
            } else {
                addToDatabase(p);
                return null;
            }
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

    public int[] getWaitingRequestsArray(final OfflineRPlayer p) {
        try {
            if(getWaitingRequests(p) != null) {
                return StringUtil.stringToIntArray(getWaitingRequests(p).split("\\|"));
            } else {
                addToDatabase(p);
                return null;
            }
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
            return null;
        }
    }

}
