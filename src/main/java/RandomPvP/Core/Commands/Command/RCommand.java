package RandomPvP.Core.Commands.Command;

import RandomPvP.Core.Player.MsgType;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import RandomPvP.Core.Util.NetworkUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * ***************************************************************************************
 * Copyright (c) Randomizer27 2014. All rights reserved.
 * All code contained within this document and any APIs assocated are
 * the sole property of Randomizer27. Please do not distribute/reproduce without
 * expressed explicit permission from Randomizer27. Not doing so will break the terms of
 * the license, and void any agreements with you, the third party.
 * Thanks.
 * ***************************************************************************************
 */
public abstract class RCommand extends Command {

    private String name;
    private String[] args;
    private Rank needed = Rank.PLAYER;
    private boolean playerOnly = false;
    private int minimumArgs = 0;
    private int maximumArgs = -1;

    public RCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {
        try {
            checkCommand(sender, s, strings);
            args = strings;
        } catch (CommandException e) {
            sender.sendMessage(MsgType.ERROR.getPrefix() + e.getMessage());
        }

        return false;
    }

    public void checkCommand(CommandSender sender, String s, String[] args) throws CommandException {
        if (playerOnly) {
            if (sender instanceof Player) {
                RPlayer player = PlayerManager.getInstance().getPlayer((Player) sender);

                if (!player.has(needed)) {
                    throw new CommandException("You need " + needed.getFormattedName() + " §7to use this command!");
                }
            } else {
                throw new CommandException("Only players can use this command!");
            }
        } else {
            if (sender instanceof Player) {
                RPlayer player = PlayerManager.getInstance().getPlayer((Player) sender);
                if (!player.has(needed)) {
                    throw new CommandException("You need " + needed.getFormattedName() + " §7to use this command!");
                }
            }
        }

        if ((args.length > maximumArgs && maximumArgs != -1) || args.length < minimumArgs) {
            throw new CommandException(getUsage());
        }

        if (playerOnly) {
            try {
                onCommand(PlayerManager.getInstance().getPlayer((Player) sender), s, args);
            } catch (Exception ex) {
                NetworkUtil.handleErrorMessage(PlayerManager.getInstance().getPlayer((Player) sender), ex);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "I'm sorry, but there is not console/player access yet.");
            //onCommand(PlayerManager.getInstance().getConsole(), s, args);
        }
    }

    public abstract void onCommand(RPlayer pl, String string, String[] args);

    public String getString(int where) {
        return args[where];
    }

    public String getJoinedString(int where) {
        StringBuilder buffer = new StringBuilder(args[where]);

        for (int i = args.length + 1; i<args.length; ++i) {
            buffer.append(" ").append(args[i]);
        }

        return buffer.toString();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(Rank rank) {
        needed = rank;
    }

    public void setPlayerOnly(boolean b) {
        playerOnly = b;
    }

    public void setMinimumArgs(int num) {
        minimumArgs = num;
    }

    public void setMaximumArgs(int num) {
        maximumArgs = num;
    }

    public void setArgsUsage(String msg) {
        setUsage("" + "/" + getName() + " " + msg + " - " + (getDescription() != null ? getDescription() : "No description."));
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public Rank getRankNeeded() {
        return needed;
    }
}
