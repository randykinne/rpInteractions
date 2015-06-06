package RandomPvP.Core.Server.General.Poll;

import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Util.Color.CC;
import RandomPvP.Core.Server.General.Poll.Exception.PollException;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Util.NetworkUtil;
import RandomPvP.Core.Util.StringUtil;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
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
public class PollManager {

    public void createPoll(final String name, final String question, final String option1, final String option2, final String option3, final String option4) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE `name`=?;");
                    stmt.setString(1, name);
                    if(!stmt.executeQuery().next()) {
                        PreparedStatement insert = MySQL.getConnection().prepareStatement("INSERT INTO `polls` VALUES (?,?,?,?,?,?,?,?,?,?,?,?);");
                        insert.setString(1, name);
                        insert.setString(2, question);
                        insert.setString(3, option1);
                        insert.setString(4, option2);
                        insert.setString(5, option3);
                        insert.setString(6, option4);
                        insert.setInt(7, 0);
                        insert.setInt(8, 0);
                        insert.setInt(9, 0);
                        insert.setInt(10, 0);
                        insert.setString(11, "");
                        insert.setBoolean(12, true);
                        insert.executeUpdate();
                    } else {
                        activatePoll(name);
                    }
                } catch (Exception ex) {
                    NetworkUtil.handleError(ex);
                }
            }

        }.start();
    }

    public void addVote(final String poll, final int option, final RPlayer p) throws PollException {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE `name`=?;");
                    stmt.setString(1, poll);
                    ResultSet res = stmt.executeQuery();
                    if(res.next() && res.getBoolean("active")) {
                        PreparedStatement update = MySQL.getConnection().prepareStatement("UPDATE `polls` SET votes" + option + "=?, voted=? WHERE name=?;");
                        update.setInt(1, res.getInt("votes" + option) + 1);
                        update.setString(2, res.getString("voted") + "|" + p.getRPID());
                        update.setString(3, poll);
                        update.executeUpdate();
                    } else {
                        throw new PollException(p, "There is no such poll under the name of " + poll + " or it isn't currently active.");
                    }
                } catch (Exception ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public List<String> getOpenPolls() {
        try {
            Future<List<String>> task = Executors.newCachedThreadPool().submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `name` FROM `polls` WHERE `active`=?");
                    stmt.setBoolean(1, true);
                    ResultSet res = stmt.executeQuery();
                    List<String> polls = new ArrayList<String>();
                    while (res.next()) {
                        polls.add(res.getString("name"));
                    }
                    return polls;
                }
            });
            return task.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void deactivatePoll(final String name) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE name=?;");
                    stmt.setString(1, name);
                    ResultSet res = stmt.executeQuery();
                    if(res.next() && res.getBoolean("active")) {
                        PreparedStatement update = MySQL.getConnection().prepareStatement("UPDATE `polls` SET `active`=? WHERE `name`=?;");
                        update.setBoolean(1, false);
                        update.setString(2, name);
                        update.executeUpdate();
                    }
                } catch (Exception ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void activatePoll(final String name) {
        new Thread() {
            public void run() {
                try {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE name=?;");
                    stmt.setString(1, name);
                    ResultSet res = stmt.executeQuery();
                    if(res.next() && !res.getBoolean("active")) {
                        PreparedStatement update = MySQL.getConnection().prepareStatement("UPDATE `polls` SET `active`=? WHERE `name`=?;");
                        update.setBoolean(1, true);
                        update.setString(2, name);
                        update.executeUpdate();
                    }
                } catch (Exception ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public void deletePoll(final String name) {
        new Thread() {
            public void run() {
                try {
                    if(isValidPoll(name)) {
                        PreparedStatement update = MySQL.getConnection().prepareStatement("DELETE FROM `polls` WHERE `name`=?;");
                        update.setString(1, name);
                        update.executeUpdate();
                    }
                } catch (Exception ex) {
                    NetworkUtil.handleError(ex);
                }
            }
        }.start();
    }

    public boolean isValidPoll(final String name) {
        try {
            Future<Boolean> task = Executors.newCachedThreadPool().submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE `name`=?;");
                    stmt.setString(1, name);
                    return stmt.executeQuery().next();
                }
            });
            return task.get();
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
        }
        return false;
    }

    public boolean hasVoted(final String poll, final RPlayer p) {
        try {
            Future<Boolean> task = Executors.newCachedThreadPool().submit(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    try {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE `name`=?;");
                        stmt.setString(1, poll);
                        ResultSet res = stmt.executeQuery();
                        if (res.next()) {
                            int[] array = StringUtil.stringToIntArray(res.getString("voted").split("\\|"));
                            for(int s : array) {
                                if(s == p.getRPID()) {
                                    return true;
                                }
                            }
                        }
                    } catch (Exception ex) {
                        NetworkUtil.handleError(ex);
                    }
                    return false;
                }
            });
            return task.get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public HashMap<String, Integer> getData(final String poll) {
        try {
            Future<HashMap<String, Integer>> task = Executors.newCachedThreadPool().submit(new Callable<HashMap<String, Integer>>() {
                @Override
                public HashMap<String, Integer> call() throws Exception {
                    try {
                        PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `polls` WHERE `name`=?");
                        stmt.setString(1, poll);
                        ResultSet res = stmt.executeQuery();
                        HashMap<String, Integer> data = new HashMap<String, Integer>();
                        {
                            for (int i = 0; i < 4; i++) {
                                if (i != 0 && res.next()) {
                                    data.put("option1", res.getInt("votes" + i));
                                }
                            }
                        }
                        return data;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }
            });
            return task.get();
        } catch (Exception ex) {
            NetworkUtil.handleError(ex);
        }
        return null;
    }

    public void sendPollMessage(RPlayer p, String poll) {
        try {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT `question`,`option1`, `option2`, `option3`,`option4` FROM `polls` WHERE `name`=?");
            stmt.setString(1, poll);
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                IChatBaseComponent msg1 = new ChatMessage("§8§l>§7 1. " + res.getString("option1"));
                {
                    ChatModifier modifier = new ChatModifier();
                    modifier.setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, "/poll vote " + poll + " 1"));
                    modifier.a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage("Click to vote for Option 1")));
                    msg1.setChatModifier(modifier);
                }
                IChatBaseComponent msg2 = new ChatMessage("§8§l>§7 2. " + res.getString("option2"));
                {
                    ChatModifier modifier = new ChatModifier();
                    modifier.setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, "/poll vote " + poll + " 2"));
                    modifier.a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage("Click to vote for Option 2")));
                    msg2.setChatModifier(modifier);
                }
                IChatBaseComponent msg3 = new ChatMessage("§8§l>§7 3. " + res.getString("option3"));
                {
                    ChatModifier modifier = new ChatModifier();
                    modifier.setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, "/poll vote " + poll + " 3"));
                    modifier.a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage("Click to vote for Option 3")));
                    msg3.setChatModifier(modifier);
                }
                IChatBaseComponent msg4 = new ChatMessage("§8§l>§7 4. " + res.getString("option4"));
                {
                    ChatModifier modifier = new ChatModifier();
                    modifier.setChatClickable(new ChatClickable(EnumClickAction.RUN_COMMAND, "/poll vote " + poll + " 4"));
                    modifier.a(new ChatHoverable(EnumHoverAction.SHOW_TEXT, new ChatMessage("Click to vote for Option 4")));
                    msg4.setChatModifier(modifier);
                }
                p.getPlayer().playSound(p.getLocation(), Sound.ANVIL_LAND, 1F, 1F);
                p.message(MsgType.INFO, CC.DGRN.toString() + CC.BLD + "POLL: " + CC.GRN + res.getString("question"));
                sendChatPacket(p, msg1);
                sendChatPacket(p, msg2);
                sendChatPacket(p, msg3);
                sendChatPacket(p, msg4);
            } else {
                p.message(MsgType.ERROR, "Could not load poll data for poll " + poll + ".");
            }
        } catch (Exception ex) {
            p.message(MsgType.ERROR, "Could not load poll data for poll " + poll + ".");
            NetworkUtil.handleError(ex);
        }
    }

    private void sendChatPacket(RPlayer p, IChatBaseComponent component) {
        PacketPlayOutChat packet = new PacketPlayOutChat(component, (byte)0);
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

}
