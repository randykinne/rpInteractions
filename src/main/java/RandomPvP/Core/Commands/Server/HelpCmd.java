package RandomPvP.Core.Commands.Server;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Commands.Command.RCommandMap;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class HelpCmd extends RCommand {

    public HelpCmd() {
        super("help");
        setPlayerOnly(true);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        int page = 1;
        if(args.length != 0) {
            try {
                page = Integer.valueOf(args[0]);
            } catch (Exception ex) {
                pl.message(MsgType.ERROR, "You must supply a valid page number.");
                return;
            }
        }

        pl.message(MsgType.INFO, "Showing all available commands to you... (Page " + ChatColor.DARK_RED + page + ChatColor.GRAY + ")");

        List<RCommand> cmds = new ArrayList<>();
        {
            for (RCommand cmd : RCommandMap.getCommands()) {
                if (pl.has(cmd.getRankNeeded())) {
                    cmds.add(cmd);
                }
            }
        }

        //53
        //47

        List<RCommand> display = cmds.size() >= 9 ? cmds.subList(getStartingPoint(page), getStartingPoint(page+1)-1) : cmds;
        for(RCommand cmd : display) {
            pl.message(MsgType.INFO, ChatColor.WHITE + "- " + ChatColor.GRAY + "/" + cmd.getName() + " - " +
                    (cmd.getDescription().length() > 47-cmd.getName().length() ? cmd.getDescription().substring(0, 44-cmd.getName().length())+"..." : cmd.getDescription()));
        }
    }

    private int getStartingPoint(int page) {
        switch (page) {
            case 1:
                return 0;
            case 2:
                return 10;
            case 3:
                return 20;
        }

        return 0;
    }

}
