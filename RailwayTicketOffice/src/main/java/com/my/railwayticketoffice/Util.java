package com.my.railwayticketoffice;

/**
 * Util class that contains some different variables
 */
public class Util {

    private static int scheduleDuration = 14;

    /**
     * Returns the number of days contained in the train schedule
     * @return int
     */
    public static int getScheduleDuration() {
        return scheduleDuration;
    }

    /**
     * Change the number of days contained in the train schedule
     */
    public static void setScheduleDuration(int scheduleDuration) {
        if (scheduleDuration > 0) {
            Util.scheduleDuration = scheduleDuration;
        }
    }
}
