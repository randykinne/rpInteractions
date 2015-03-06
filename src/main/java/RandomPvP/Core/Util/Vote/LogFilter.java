package RandomPvP.Core.Util.Vote;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

class LogFilter
        implements Filter
{
    private String prefix;

    public LogFilter(String prefix)
    {
        this.prefix = prefix;
    }

    public boolean isLoggable(LogRecord record)
    {
        record.setMessage(this.prefix + record.getMessage());
        return true;
    }
}
