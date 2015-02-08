package RandomPvP.Core.Player.Scoreboard;

import RandomPvP.Core.Game.GameManager;
import RandomPvP.Core.Game.Team.*;
import RandomPvP.Core.Player.PlayerManager;
import RandomPvP.Core.Player.RPlayer;
import RandomPvP.Core.Player.Rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import org.bukkit.scoreboard.Team;

public class RandomPvPScoreboard {

    public static void updateScoreboard(boolean force) {
        // Its in lobby so everyone needs their own scoreboards
        for (RPlayer p : PlayerManager.getInstance().getOnlinePlayers()) {
            updateScorebard(p.getPlayer(), force);
        }
    }

    public static void updateScorebard(Player p, boolean force) {
        if (!p.isOnline() || p.isDead()) {
            return;
        }
        if (PlayerManager.getInstance().getPlayer(p) == null || PlayerManager.getInstance().getPlayer(p).getPlayer() == null)
            return;

        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
        if (p.getScoreboard() != null && !force) {
            sb = p.getScoreboard();
            if (sb.getObjective(DisplaySlot.SIDEBAR) != null) {
                sb.getObjective(DisplaySlot.SIDEBAR).unregister();
            }
        }

        Objective obj = sb.registerNewObjective("info", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("");

        sb = assignPlayerTeams(sb);
        if (force || p.getScoreboard() == null) {
            p.setScoreboard(sb);
        }
    }

    public static ChatColor getRandomColor(int line) {
        if (line == 3)
            return ChatColor.AQUA;
        if (line == 6)
            return ChatColor.LIGHT_PURPLE;
        if (line == 9)
            return ChatColor.GREEN;
        if (line == 12)
            return ChatColor.RED;

        return ChatColor.BLUE;
    }

    public static Scoreboard assignPlayerTeams(Scoreboard scoreboard) {
        if (scoreboard.getTeams().isEmpty()) {
            for (Rank tag : Rank.values()) {
                // In game so register these extra teams
                for (RandomPvP.Core.Game.Team.Team t : TeamManager.getTeams().values()) {
                    scoreboard.registerNewTeam(tag.name() + t.getColor().name()).setPrefix(
                            (tag != Rank.PLAYER ? (tag.getName()) : "") + t.getColor());
                }

                scoreboard.registerNewTeam(tag.name()).setPrefix((tag != Rank.PLAYER ? (tag.getTag() + ChatColor.RESET + "") : ""));
            }
        }

        // Setup scoreboard
        for (RPlayer pl : PlayerManager.getInstance().getOnlinePlayers()) {
            Player p = pl.getPlayer();
            if (p == null || !p.isOnline() || PlayerManager.getInstance().getPlayer(p) == null || PlayerManager.getInstance().getPlayer(p).getPlayer() == null)
                continue;
            Team team = null;
            // Its starting so make it just the reg rank with no color.
            if (pl.getTeam() != null || !GameManager.getState().getName().equalsIgnoreCase("Lobby")) {
                team = scoreboard.getTeam(PlayerManager.getInstance().getPlayer(p).getRank().name());
            } else {
                team = scoreboard.getTeam(PlayerManager.getInstance().getPlayer(p).getRank().name() + PlayerManager.getInstance().getPlayer(p).getTeam().getColor().name());
            }
            if (team == null) {
                team = scoreboard.getTeam(Rank.PLAYER.getName());
            }

            team.setCanSeeFriendlyInvisibles(false);
            if (!team.hasEntry(p.getName())) {
                team.addPlayer(p);
            }
        }
        return scoreboard;
    }

    private static String score(String name) {
        return name;
    }

    public static class ScoreboardLines {

        private Objective obj;
        private String[] lines;

        public ScoreboardLines(Objective obj, int size) {
            this.obj = obj;
            this.lines = new String[size];
        }

        public void addLine(String line, int lineNumber) {
            this.lines[lineNumber] = line;
        }

        public void build() {
            int i = lines.length;
            for (String line : this.lines) {
                if (line == null) {
                    Score s = obj.getScore(score((ChatColor.values()[i].toString())));
                    s.setScore(i--);
                    continue;
                }
                if (line.length() > 16) {
                    line = line.substring(0, 16);
                }
                Score s = obj.getScore(score(line));
                s.setScore(i--);
            }
        }

    }
}
