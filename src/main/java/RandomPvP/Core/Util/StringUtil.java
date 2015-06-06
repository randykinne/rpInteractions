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

    //from apache commons
    public static String join(Object[] array, String separator, int startIndex, int endIndex)
    {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
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

    public static String booleanToString(boolean b) {
        String bool;
        {
            if(b) bool = "TRUE";
            else bool = "FALSE";
        }
        return bool;
    }

    public static String removeDisallowedCharacters(char[] allowed, String input) {
        char[] charArray = input.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : charArray) {
            for (char a : allowed) {
                if(c==a) result.append(a);
            }
        }
        return result.toString();
    }

}
