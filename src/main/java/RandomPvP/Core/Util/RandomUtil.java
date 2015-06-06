package RandomPvP.Core.Util;

import java.util.Random;

/**
 * ****************************************************************************************
 * All code contained within this document is sole property of WesJD. All rights reserved.*
 * Do NOT distribute/reproduce any of this code without permission from WesJD.            *
 * Not following this statement will result in a void of all agreements made.             *
 * Enjoy.                                                                                 *
 * ****************************************************************************************
 */
public class RandomUtil {

    private Random r = new Random();

    public int getRandomNumber(int min, int max) {
        int i = r.nextInt(max);
        while (i < min) {
            i = r.nextInt(max);
        }
        return i;
    }

}
