package RandomPvP.Core.Punish;

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
public enum Warning {

    SPEC_BLOCKING("Spec Blocking", "Do not abuse spectator mode!", true),
    BLOCK_GLITCHING("Block Glitching", "Do not block glitch!", false);

    String name;
    String desc;
    boolean setFrozen;
    Warning(String name, String desc, boolean setFrozen) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desc;
    }

    public boolean isSetFrozen() {
        return setFrozen;
    }
}
