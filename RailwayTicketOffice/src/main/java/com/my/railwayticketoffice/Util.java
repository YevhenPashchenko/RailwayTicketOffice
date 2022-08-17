package com.my.railwayticketoffice;

/**
 * Util class that contains some different variables.
 *
 * @author Yevhen Pashchenko
 */
public class Util {

    private static int scheduleDuration = 14;
    private static int basicTicketCost = 50;
    private static double oneKilometerRoadCost = 0.5;

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

    public static int getBasicTicketCost() {
        return basicTicketCost;
    }

    public static void setBasicTicketCost(int basicTicketCost) {
        Util.basicTicketCost = basicTicketCost;
    }

    public static double getOneKilometerRoadCost() {
        return oneKilometerRoadCost;
    }

    public static void setOneKilometerRoadCost(double oneKilometerRoadCost) {
        Util.oneKilometerRoadCost = oneKilometerRoadCost;
    }
}
