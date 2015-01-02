package RandomPvP.Core.Util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class DateUtil {

    public static long getTimeStamp(String time) {
        long timeReturn;
        try {
            timeReturn = parseDateDiff(time, true);
        }
        catch (Exception e) {
            timeReturn = 0L;
        }
        return timeReturn;
    }

    public static long parseDateDiff(String time, boolean future)
            throws Exception
    {
        Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find()) {
            if ((m.group() == null) || (m.group().isEmpty())) {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++) {
                if ((m.group(i) != null) && (!m.group(i).isEmpty())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if ((m.group(1) != null) && (!m.group(1).isEmpty()))
                    years = Integer.parseInt(m.group(1));
                if ((m.group(2) != null) && (!m.group(2).isEmpty()))
                    months = Integer.parseInt(m.group(2));
                if ((m.group(3) != null) && (!m.group(3).isEmpty()))
                    weeks = Integer.parseInt(m.group(3));
                if ((m.group(4) != null) && (!m.group(4).isEmpty()))
                    days = Integer.parseInt(m.group(4));
                if ((m.group(5) != null) && (!m.group(5).isEmpty()))
                    hours = Integer.parseInt(m.group(5));
                if ((m.group(6) != null) && (!m.group(6).isEmpty()))
                    minutes = Integer.parseInt(m.group(6));
                if ((m.group(7) == null) || (m.group(7).isEmpty())) break;
                seconds = Integer.parseInt(m.group(7));
            }
        }

        if (!found) {
            throw new Exception("Illegal Date");
        }
        if (years > 20) {
            throw new Exception("Illegal Date");
        }
        Calendar c = new GregorianCalendar();
        if (years > 0)
            c.add(1, years * (future ? 1 : -1));
        if (months > 0)
            c.add(2, months * (future ? 1 : -1));
        if (weeks > 0)
            c.add(3, weeks * (future ? 1 : -1));
        if (days > 0)
            c.add(5, days * (future ? 1 : -1));
        if (hours > 0)
            c.add(11, hours * (future ? 1 : -1));
        if (minutes > 0)
            c.add(12, minutes * (future ? 1 : -1));
        if (seconds > 0)
            c.add(13, seconds * (future ? 1 : -1));
        return c.getTimeInMillis();
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) {
            return "Now";
        }
        if (toDate.after(fromDate)) {
            future = true;
        }

        StringBuilder sb = new StringBuilder();
        int[] types = { 1, 2, 5, 11, 12, 13 };
        String[] names = { "y", "y", "m", "m", "d", "d", "h", "h", "m", "m", "s", "s" };
        for (int i = 0; i < types.length; i++) {
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0) {
                sb.append(" ").append(diff).append(" ").append(names[(i * 2 + 0)]);
            }
        }
        if (sb.length() == 0) {
            return "now";
        }
        return sb.toString().trim();
    }

    public static String formatDateDiff(long date) {
        Calendar now = new GregorianCalendar();
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        return formatDateDiff(now, c);
    }

    private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while (((future) && (!fromDate.after(toDate))) || ((!future) && (!fromDate.before(toDate)))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }
}
