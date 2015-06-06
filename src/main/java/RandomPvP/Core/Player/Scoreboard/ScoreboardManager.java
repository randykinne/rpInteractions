package RandomPvP.Core.Player.Scoreboard;

import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Server.Game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class ScoreboardManager {

    private static ScoreboardManager manager;
    private HashMap<RPlayer, HashMap<String, Integer>> lines = new HashMap<>();
    private HashMap<RPlayer, String> titles = new HashMap<>();
    private boolean showCredits = true;

    public void setLine(RPlayer pl, String score, int value) {
        HashMap<String, Integer> base = (lines.containsKey(pl) ? lines.get(pl) : new HashMap<String, Integer>());
        {
            base.put(score, value);
        }
        lines.put(pl, base);
    }

    public void setLineForAll(String score, int value) {
        for(RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            setLine(pl, score, value);
        }
    }

    public void removeLine(RPlayer pl, String score) {
        if(lines.containsKey(pl)) {
            lines.get(pl).remove(score);
        }
    }

    public void removePlayer(RPlayer pl) {
        if(lines.containsKey(pl)) {
            lines.remove(pl);
        }
    }

    public void updateScoreboard(RPlayer p) {
        org.bukkit.scoreboard.Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        String ab;
        {
            if(GameManager.getGame().getScoreboardAbbreviation().length() > 5) {
                ab = GameManager.getGame().getScoreboardAbbreviation().substring(0, 5);
            } else {
                ab = GameManager.getGame().getScoreboardAbbreviation();
            }
        }

        Objective obj = sb.registerNewObjective(p.getName(), "dummy");
        {
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);

            if(!titles.containsKey(p)) {
                obj.setDisplayName(p.getRank().getColor() + p.getName() + " §8|| §7" + ab);
            } else {
                obj.setDisplayName(titles.get(p));
            }

            if(showCredits) obj.getScore("§aCredits").setScore(p.getCredits());

            if(lines.containsKey(p)) {
                for (String score : lines.get(p).keySet()) {
                    obj.getScore(score).setScore(lines.get(p).get(score));
                }
            }
        }

        p.getPlayer().setScoreboard(sb);
    }

    public void updateScoreboardForAll() {
        for(RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            updateScoreboard(pl);
        }
    }

    public boolean showingCredits() {
        return showCredits;
    }

    public void setShowCredits(boolean showCredits) {
        this.showCredits = showCredits;
    }

    public String getDisplayName(RPlayer pl) {
        if(titles.containsKey(pl)) {
            return titles.get(pl);
        } else {
            return pl.getRank().getColor() + pl.getName() + " §8|| §7" + GameManager.getGame().getScoreboardAbbreviation();
        }
    }

    public void setDisplayName(RPlayer pl, String title) {
        titles.put(pl, title);
    }

    public static ScoreboardManager getInstance() {
        if(manager == null) manager = new ScoreboardManager();
        return manager;
    }
}
