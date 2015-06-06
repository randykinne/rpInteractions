package RandomPvP.Core.Commands.Admin;

import RandomPvP.Core.Commands.Command.RCommand;
import RandomPvP.Core.Player.Counter.Counter;
import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.RPICore;
import RandomPvP.Core.Server.General.Poll.Exception.PollException;
import RandomPvP.Core.Server.General.Poll.PollManager;
import RandomPvP.Core.Util.Color.CC;
import RandomPvP.Core.Server.General.ServerToggles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class PollCmd extends RCommand implements Listener {

    private ArrayList<RPlayer> setup = new ArrayList<>();
    private HashMap<RPlayer, String[]> data = new HashMap<>();

    public PollCmd() {
        super("poll");
        setRank(Rank.ADMIN);
        setPlayerOnly(true);
        setMinimumArgs(1);

        Bukkit.getServer().getPluginManager().registerEvents(this, RPICore.getInstance());
    }

    @Override
    public void onCommand(RPlayer pl, String string, String[] args) {
        if(ServerToggles.pollVotingEnabled()) {
            if (args[0].equalsIgnoreCase("create")) {
                if (setup.contains(pl)) {
                    pl.message(MsgType.ERROR, "You are already creating a poll! Type the answer to the query!");
                } else {
                    setup.add(pl);
                    pl.message(MsgType.INFO, ChatColor.RED + "Starting poll setup... Just type your answers into chat. You can type \"cancel\" to stop creating it!");
                    pl.message(MsgType.INFO, "What would you like the poll ID to be?");
                    pl.addCounter(new Counter("pollsetup"));
                    pl.getCounter("pollsetup").setLevel(0);
                }
            } else if (args[0].equalsIgnoreCase("vote")) {
                if (args.length == 3) {
                    PollManager manager = new PollManager();
                    if (manager.isValidPoll(args[1])) {
                        if (!manager.hasVoted(args[1], pl)) {
                            int option;
                            {
                                try {
                                    option = Integer.valueOf(args[2]);
                                    if (option > 4) {
                                        pl.message(MsgType.ERROR, "You must supply a valid option.");
                                        return;
                                    }
                                } catch (Exception ex) {
                                    pl.message(MsgType.ERROR, "You must supply a valid option.");
                                    return;
                                }
                            }
                            try {
                                manager.addVote(args[1], option, pl);
                                pl.getPlayer().playSound(pl.getLocation(), Sound.CLICK, 1F, 1F);
                                pl.message(MsgType.INFO, "Thanks for voting!");
                            } catch (PollException handled) {
                            }
                        } else {
                            pl.message(MsgType.ERROR, "You have already voted for this poll.");
                        }
                    } else {
                        pl.message(MsgType.ERROR, "You must supply a valid poll.");
                    }
                } else {
                    pl.message(MsgType.ERROR, "You must supply the needed arguments.");
                }
            } else if (args[0].equalsIgnoreCase("activate")) {
                if (args.length == 2) {
                    PollManager manager = new PollManager();
                    if (manager.isValidPoll(args[1])) {
                        manager.activatePoll(args[1]);
                        pl.message(MsgType.INFO, "Successfully enabled the poll.");
                    } else {
                        pl.message(MsgType.ERROR, "You must supply a valid poll.");
                    }
                } else {
                    pl.message(MsgType.ERROR, "You must supply a poll.");
                }
            } else if (args[0].equalsIgnoreCase("deactivate")) {
                if (args.length == 2) {
                    PollManager manager = new PollManager();
                    if (manager.isValidPoll(args[1])) {
                        manager.deactivatePoll(args[1]);
                        pl.message(MsgType.INFO, "Successfully disabled the poll.");
                    } else {
                        pl.message(MsgType.ERROR, "You must supply a valid poll.");
                    }
                } else {
                    pl.message(MsgType.ERROR, "You must supply a poll.");
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                if (args.length == 2) {
                    PollManager manager = new PollManager();
                    if (manager.isValidPoll(args[1])) {
                        manager.deletePoll(args[1]);
                        pl.message(MsgType.INFO, "Successfully deleted the poll.");
                    } else {
                        pl.message(MsgType.ERROR, "You must supply a valid poll.");
                    }
                } else {
                    pl.message(MsgType.ERROR, "You must supply a poll.");
                }
            }
        } else {
            pl.message(MsgType.ERROR, "Polls are not enabled here.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e) {
        RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
        if(setup.contains(pl)) {
            e.setCancelled(true);
            if(pl.getCounter("pollsetup") != null) {
                String msg = e.getMessage();
                Counter counter = pl.getCounter("pollsetup");
                if(msg.equalsIgnoreCase("cancel")) {
                    pl.removeCounter(new Counter("pollsetup"));
                    setup.remove(pl);
                    data.remove(pl);
                    pl.message(MsgType.INFO, "Successfully stopped creating the poll.");
                } else if(counter.getLevel() == 0) {
                    if(new PollManager().isValidPoll(msg)) {
                        pl.message(MsgType.ERROR, "There is already a poll by that name.");
                        return;
                    }
                    if(msg.contains(" ")) {
                        pl.message(MsgType.ERROR, "The poll ID cannot be more than one word.");
                        return;
                    }
                    String[] array = new String[10];
                    {
                        array[0] = msg;
                        data.put(pl, array);
                    }
                    counter.setLevel(1);
                    pl.message(MsgType.INFO, "What would you like the poll question to be?");
                } else if(counter.getLevel() == 1) {
                    String[] array = data.get(pl);
                    {
                        array[1] = msg;
                        data.put(pl, array);
                    }
                    counter.setLevel(2);
                    pl.message(MsgType.INFO, "What would you like option 1 to be?");
                } else if(counter.getLevel() == 2) {
                    String[] array = data.get(pl);
                    {
                        array[2] = msg;
                        data.put(pl, array);
                    }
                    counter.setLevel(3);
                    pl.message(MsgType.INFO, "What would you like option 2 to be?");
                } else if(counter.getLevel() == 3) {
                    String[] array = data.get(pl);
                    {
                        array[3] = msg;
                        data.put(pl, array);
                    }
                    counter.setLevel(4);
                    pl.message(MsgType.INFO, "What would you like option 3 to be?");
                } else if(counter.getLevel() == 4) {
                    String[] array = data.get(pl);
                    {
                        array[4] = msg;
                        data.put(pl, array);
                    }
                    counter.setLevel(5);
                    pl.message(MsgType.INFO, "What would you like option 4 to be?");
                } else if(counter.getLevel() == 5) {
                    String[] array = data.get(pl);
                    {
                        array[5] = msg;
                        data.put(pl, array);
                    }
                    pl.message(MsgType.INFO, ChatColor.GREEN + "All done! Thanks for setting up the poll!");
                    pl.message(MsgType.INFO, ChatColor.GREEN + "Poll activated.");
                    {
                        PollManager manager = new PollManager();
                        String[] answers = data.get(pl);
                        manager.createPoll(answers[0], answers[1], answers[2], answers[3], answers[4], answers[5]);
                    }
                    pl.removeCounter(new Counter("pollsetup"));
                    setup.remove(pl);
                    data.remove(pl);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        RPlayer pl = PlayerManager.getInstance().getPlayer(e.getPlayer());
        if (data.containsKey(pl)) {
            pl.removeCounter(new Counter("pollsetup"));
            setup.remove(pl);
            data.remove(pl);
        }
    }

}
