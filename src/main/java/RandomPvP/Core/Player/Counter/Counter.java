package RandomPvP.Core.Player.Counter;

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
public class Counter {

    String name;
    int level;

    public Counter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void removeLevel() {
        level = level - 1;
    }

    public void addLevel() {
        level = level + 1;
    }

    public void setLevel(int l) {
        level = l;
    }

    public int getLevel() {
        return level;
    }
}
