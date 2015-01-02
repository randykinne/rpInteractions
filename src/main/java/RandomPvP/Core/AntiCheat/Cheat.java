package RandomPvP.Core.AntiCheat;

import RandomPvP.Core.Player.RPlayer;

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
public interface Cheat {

    public String getName();
    public boolean isPvPRelated();
    public void check(RPlayer player);
}
