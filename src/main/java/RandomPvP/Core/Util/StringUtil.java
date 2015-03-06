package RandomPvP.Core.Util;

import RandomPvP.Core.Player.MsgType;

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
public class StringUtil {

    public static String join(String[] string) {
        return join(string, " ");
    }

    public static String join(String[] string, String separator) {
        StringBuilder str = new StringBuilder();
        for (String s : string) {
            str.append(separator);
            str.append(s);
        }

        return str.toString();
    }

    public String formatLongMessage(MsgType type, String msg) {
        return null;
    }

    public static int[] stringToIntArray(String[] array) {
        int[] a = new int[array.length];
        for(int i=0; i < array.length; i++) {
            try {
                a[i] = Integer.valueOf(array[i]);
            } catch (Exception ignored) {}
        }
        return a;
    }

}
