package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Data.MySQL;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.NetworkUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ErrorFixedCmd extends RCommand {

    public ErrorFixedCmd() {
        super("errorfixed");
        setPlayerOnly(true);
        setArgsUsage("<Error> <Cause> <Server> <Fixed>");
        setMinimumArgs(4);
        setRank(Rank.ADMIN);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        try {
            PreparedStatement stmt = MySQL.getConnection().prepareStatement("SELECT * FROM `errors` WHERE `error`=? && `cause`=? && `server`=?");
            {
                stmt.setString(1, args[0]);
                stmt.setString(2, args[1]);
                stmt.setString(3, args[2]);
            }
            ResultSet res = stmt.executeQuery();

            if(res.next()) {
                boolean fixed;
                {
                    try {
                        fixed = Boolean.valueOf(args[3]);
                    } catch (Exception ex) {
                        pl.message(MsgType.ERROR, "You must supply a valid boolean.");
                        return;
                    }
                }

                PreparedStatement update = MySQL.getConnection().prepareStatement("UPDATE `errors` SET `fixed`=? WHERE `error`=? && `cause`=? && `server`=?");
                {
                    update.setBoolean(1, fixed);
                    update.setString(2, args[0]);
                    update.setString(3, args[1]);
                    update.setString(4, args[2]);
                }
                update.executeUpdate();

                pl.message(MsgType.INFO, "Successfully set the error fixed status to " + fixed + ".");
            } else {
                pl.message(MsgType.ERROR, "Invalid error to edit.");
            }
        } catch (SQLException ex) {
            NetworkUtil.handleErrorMessage(pl, ex);
        }
    }

}
