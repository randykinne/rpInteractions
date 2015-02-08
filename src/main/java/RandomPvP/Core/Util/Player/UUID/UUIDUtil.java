package RandomPvP.Core.Util.Player.UUID;

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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDUtil {

    /**
     * Get a player's UUID
     *
     * @param player Player to get UUID from
     * @return Player's UUID
     */
    public static UUID getUUID(String player) {
        if (getUUIDs().containsKey(player)) {
            return getUUIDs().get(player);
        } else {
            UUIDFetcher fetcher = new UUIDFetcher(Arrays.asList(player));
            try {
                UUID id = fetcher.call().get(player);
                PLAYER_UUIDS.put(player, id);
                return id;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static Map<String, UUID> PLAYER_UUIDS = new HashMap<>();

    /**
     * Get the player - uuid map
     *
     * @return Map containing player names and UUID objects
     */
    public static Map<String, UUID> getUUIDs() {
        return PLAYER_UUIDS;
    }


    /**
     * Get a player's name from a UUID
     *
     * @param uuid UUID to grab name for
     * @return Player's Name
     */
    public static String getName(UUID uuid) {
        NameFetcher fetcher = new NameFetcher(Arrays.asList(uuid));
        try {
            return fetcher.call().get(uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}