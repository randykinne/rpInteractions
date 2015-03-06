package RandomPvP.Core.Util.Vote.model;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private Vote vote;

    public VoteEvent(Vote vote)
    {
        this.vote = vote;
    }

    public Vote getVote()
    {
        return this.vote;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
