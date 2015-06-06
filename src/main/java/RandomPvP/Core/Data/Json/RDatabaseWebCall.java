package RandomPvP.Core.Data.Json;

import org.bukkit.event.block.BlockPlaceEvent;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RDatabaseWebCall extends JsonWebCall {

    public RDatabaseWebCall(String database) {
        super("http://rpjson.netne.net/database_getter.php?database_name=" + database);
    }

    public void onPlace(BlockPlaceEvent e) {
        e.getBlock().getLocation().getChunk();
    }

}
