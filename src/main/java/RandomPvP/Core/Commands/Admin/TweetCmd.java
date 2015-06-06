package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.GUI.ConfirmGUI;
import RandomPvP.Core.Util.IO.Twitter;
import RandomPvP.Core.Util.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Arrays;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class TweetCmd extends RCommand {

    public TweetCmd() {
        super("tweet");
        setPlayerOnly(true);
        setArgsUsage("<msg>");
        setRank(Rank.ADMIN);
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < args.length; i++) {
                sb.append(args[i] + " ");
            }
            final String s = sb.toString();
            if(s.length() <= 139) {
                new ConfirmGUI(pl, ItemBuilder.build(Material.EGG, ChatColor.RED.toString() + ChatColor.BOLD + "POST TWEET", 1, Arrays.asList(ChatColor.GRAY + s))) {
                    @Override
                    public void accept(RPlayer pl) {
                        new Twitter().postTweet(s, "1WsVo7gcblplubybP2CsYr8Je", "5g8apIHBmKa1Myukt69QU5ugRZVAVLmZNhhoe25AZMGjgJHEa3", "3219150437-Jd8dt3xMowUPUFcIPte5oWIHHRoTIx4zijmbalC", "PXrV98Z7Rg0roPIVikrX5iJ8EzrkO6VIisnxpLGoDNPcz");
                        pl.message(MsgType.INFO, "Successfully posted the tweet.");
                    }
                };
            } else {
                pl.message(MsgType.ERROR, "A tweet can only be up to 140 characters long.");
            }
        } else {
            pl.message(MsgType.ERROR, "You must supply a message.");
        }
    }

}
