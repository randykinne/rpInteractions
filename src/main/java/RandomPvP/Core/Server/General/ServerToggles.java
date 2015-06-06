package RandomPvP.Core.Server.General;

import RandomPvP.Core.Server.Game.GameManager;
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

    private static boolean checkForBan = true;
    private static boolean chatEnabled = true;
    private static Rank rankWhitelist = Rank.PLAYER;
    private static boolean editMode = false;
    private static boolean pollVoting = false;
    private static boolean showErrors = false;

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
            GameManager.updateMotd();
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

    public static boolean pollVotingEnabled() {
        return pollVoting;
    }
    public static void setPollVoting(boolean pollVoting) {
        ServerToggles.pollVoting = pollVoting;
    }

    public static boolean isShowErrors() {
        return showErrors;
    }
    public static void setShowErrors(boolean showErrors) {
        ServerToggles.showErrors = showErrors;
    }
}
