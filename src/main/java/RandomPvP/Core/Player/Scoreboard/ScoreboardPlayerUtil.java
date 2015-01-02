package RandomPvP.Core.Player.Scoreboard;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ScoreboardPlayerUtil implements OfflinePlayer
{
    String name;

    public ScoreboardPlayerUtil(String name)
    {
        this.name = name;
    }

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Map<String, Object> serialize()
    {
        return null;
    }

    @Override
    public UUID getUniqueId()
    {
        return UUID.randomUUID();
    }

    @Override
    public boolean isOp()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setOp(boolean value)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public Location getBedSpawnLocation()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getFirstPlayed()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long getLastPlayed()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Player getPlayer()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasPlayedBefore()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBanned()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isOnline()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isWhitelisted()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setBanned(boolean banned)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setWhitelisted(boolean value)
    {
        // TODO Auto-generated method stub

    }
}