package RandomPvP.Core.Util;

import RandomPvP.Core.Player.Rank.Rank;

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
public class ServerToggles {

    static boolean checkForBan = true;
    static boolean chatEnabled = true;
    static Rank rankWhitelist = Rank.PLAYER;
    static boolean editMode = false;

    public static void setCheckForBan(boolean boo) {
        boo = checkForBan;
    }

    public static boolean checkForBan() { return checkForBan; }

    public static void setChatEnabled(boolean chat) {
        chatEnabled = chat;
    }

    public static boolean isChatEnabled() {
        return chatEnabled;
    }

    public static void setEditMode(boolean mode) {
        editMode = mode;
        if (mode) { setRankRequired(Rank.BUILDER);} else { setRankRequired(Rank.PLAYER);  }
    }
    public static boolean isEditMode() {
        return editMode;
    }

    public static void setRankRequired(Rank rank) {
        if (rankWhitelist != rank) {
            rankWhitelist = rank;
        }
    }
    public static boolean hasRankWhitelist() {
        if (rankWhitelist != Rank.PLAYER) {
            return true;
        } else {
            return false;
        }
    }
    public static Rank getRankRequired() {
        return rankWhitelist;
    }
}
