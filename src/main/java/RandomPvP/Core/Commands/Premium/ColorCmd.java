package RandomPvP.Core.Commands.Premium;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Server.Game.GameManager;
import org.bukkit.ChatColor;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ColorCmd extends RCommand {

    public ColorCmd() {
        super("color");
        setPlayerOnly(true);
        setRank(Rank.PREMIUM);
        setMinimumArgs(1);
        setArgsUsage("<Color>");
        setDescription("Used to set your chat color");
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(!GameManager.getGame().isTeamBased()) {
            ChatColor color;
            {
                try {
                    color = ChatColor.valueOf(args[0].toUpperCase());
                } catch (Exception ex) {
                    pl.message(MsgType.ERROR, "You must supply a valid chat color. " +
                            "Valid colors are Black, Dark_Blue, Dark_Green, Dark_Aqua, Dark_Red, Dark_Purple, Gold, Gray, Dark_Gray, Blue, Green, Aqua, Red, Light_Purple, Yellow, and White");
                    return;
                }
            }
            pl.setNamecolor(color);
            pl.message(MsgType.INFO, "Success!");
        } else {
            pl.message(MsgType.ERROR, "You cannot use this command in team based servers.");
        }
    }

}
