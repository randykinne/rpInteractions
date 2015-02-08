package RandomPvP.Core.Commands.Command;

import RandomPvP.Core.RPICore;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.HashMap;

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
public class RCommandMap {

    private static HashMap<String, RCommand> commands = new HashMap<>();

    public static void register(RCommand cmd) throws NoSuchFieldException, IllegalAccessException {
        final Field f = RPICore.getInstance().getServer().getClass().getDeclaredField("commandMap");
        f.setAccessible(true);

        CommandMap cmap = (CommandMap) f.get(RPICore.getInstance().getServer());

        cmap.register(cmd.getName(), cmd);
        commands.put(cmd.getName(), cmd);
    }

    public static RCommand getCommand(String name) {
        if (commands.containsKey(name)) {
            return commands.get(name);
        }

        return null;
    }

    public static RCommand[] getCommands() {
        return (RCommand[]) commands.values().toArray();
    }
}
