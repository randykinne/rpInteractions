package RandomPvP.Core.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

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
public class NumberUtil {

    public static Location getRandomLocation(int xmax, int xmin, int zmax, int zmin) {
        /*
        Random r = new Random();

        int x;
        {
            if(xmax > -1) {
                int tX = r.nextInt(xmax);
                while (tX < xmin) {
                    tX++;
                }
                x = tX;
            } else {
                int xMax = xmax;
                int added = 0;
                while (xMax < 0) {
                    xMax++;
                    added++;
                }

                int tX = r.nextInt(xmax + added);
                while (tX < xmin) {
                    tX++;
                }
                x = (tX - added);
            }
        }
        int y = 100;
        int z;
        {
            if(zmax > -1) {
                int tZ = r.nextInt(zmax);
                while (tZ < zmin) {
                    tZ++;
                }
                z = tZ;
            } else {
                int tZ = r.nextInt(zmax + zmax);
                while (tZ < zmin) {
                    tZ++;
                }
                z = (tZ - added);
            }
        }
        */

        /*
        int x = r.nextInt(Math.abs(xmax - xmin) + 1) + xmin;
        int y = 100;
        int z = r.nextInt(Math.abs(zmax - zmin) + 1) + xmin;
        */


        final float loc = xmin + (int)(Math.random() * ((xmax - xmin) + 1));
        final int x = (int) loc;

        final float loc2 = zmin + (int)(Math.random() * ((zmax - zmin) + 1));
        final int z = (int) loc2;

        return new Location(Bukkit.getWorld("world"), x, 100, z);
    }

    public static int getSlotsNeeded(int size) {
        int needed = 54;
        if (size <= 9) {
            needed = 9;
        } else if (size > 9 && size <= 18) {
            needed = 18;
        } else if (size > 18 && size <= 27) {
            needed = 27;
        } else if (size > 27 && size <= 36) {
            needed = 36;
        } else if (size > 36 && size <= 45) {
            needed = 45;
        } else if (size > 45) {
            needed = 54;
        }

        return needed;
    }

    public static int getDistanceBetween(Location loc, Location loc2) {
        return (int) Math.sqrt((loc.getX()-loc2.getX())*(loc.getX()-loc2.getX()) + (loc.getY()-loc2.getY())*(loc.getY()-loc2.getY()) + (loc.getZ()-loc2.getZ())*(loc.getZ()-loc2.getZ()));
    }

    public static int trimNumber(double number) {
        DecimalFormat format = new DecimalFormat("#.##");
        return Integer.valueOf(format.format(number));
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String translateDuration(long duration) {
        long diff = duration;
        if (duration != -1L) {
            return TimeUtil.everythingFormat.format(new Date(duration));
            /*
            StringBuilder builder = new StringBuilder();
            if ((int) (diff / 86400000L) > 0) {
                builder.append((int) (diff / 86400000L) + "d");
            }

            if ((int) (diff / 3600000L % 24L) > 0) {
                builder.append((int) (diff / 3600000L % 24L) + "h");
            }

            if ((int) (diff / 60000L % 60L) > 0) {
                builder.append((int) (diff / 60000L % 60L) + "m");
            }

            if ((int) (diff / 1000L % 60L) > 0) {
                builder.append((int) (diff / 1000L % 60L) + "s");
            }

            return builder.toString();
            */
        } else {
            return "(Permanent)";
        }
    }

}
