package RandomPvP.Core.Game;

import RandomPvP.Core.Game.Team.Team;
import org.bukkit.ChatColor;
import org.bukkit.World;

import java.sql.SQLException;
import java.util.ArrayList;

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
public interface Game {

    public abstract Game setupGame() throws SQLException;

    public String getName();

    public ArrayList<String> getDescription();

    public ChatColor getPrimaryColor();
    public ChatColor getSecondaryColor();

    public void setTeams(ArrayList<Team> teams);

    public boolean isTeamBased();

    public void setLobby(World world);

    public int getMaxPlayers();
    public int getMinPlayers();

    public int getBufferOverflow();



}
