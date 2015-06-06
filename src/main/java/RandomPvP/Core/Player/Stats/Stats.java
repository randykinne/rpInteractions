package RandomPvP.Core.Player.Stats;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class Stats {

    private int kills;
    private int deaths;

    public Stats(int kills, int deaths) {
        this.kills = kills;
        this.deaths = deaths;

    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public double getKdr() {
        if(kills != 0 && deaths != 0) {
            return (kills / deaths);
        } else {
            return 0.00;
        }
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void addKill() {
        kills = kills + 1;
    }

    public void addDeath() {
        deaths = deaths + 1;
    }

}
