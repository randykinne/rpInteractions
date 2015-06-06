package RandomPvP.Core.Server.Game;

import RandomPvP.Core.Server.Game.Mode.Gamemode;
import org.bukkit.ChatColor;

import java.util.ArrayList;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public abstract interface Game {

    public void setupGame();

    public String getName();

    public String getDownload();

    public ArrayList<String> getDesc();

    public ChatColor getPrimaryColor();
    public ChatColor getSecondaryColor();

    public boolean isTeamBased();

    public boolean allowDefaultFlight();
    public boolean allowIncognito();

    public ArrayList<Gamemode> getModes();

    public String getScoreboardAbbreviation();

    public int getMaxPlayers();
    public int getMinPlayers();

    public int getBufferOverflow();

}
