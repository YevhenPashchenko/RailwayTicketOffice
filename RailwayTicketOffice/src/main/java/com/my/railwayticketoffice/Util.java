package com.my.railwayticketoffice;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class that contains some different variables.
 *
 * @author Yevhen Pashchenko
 */
public class Util {

    private static int scheduleDuration = 14;
    private static int basicTicketCost = 50;
    private static double oneKilometerRoadCost = 0.5;
    private static int numberTrainOnPage = 2;
    private static final Map<String, Double> coefficientsByCarriageTypes = new HashMap<>();

    static {
        coefficientsByCarriageTypes.put("Л", 2.0);
        coefficientsByCarriageTypes.put("К", 1.5);
        coefficientsByCarriageTypes.put("П", 1.0);
        coefficientsByCarriageTypes.put("С1", 1.75);
        coefficientsByCarriageTypes.put("С2", 1.5);
    }

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

    public static int getNumberTrainOnPage() {
        return numberTrainOnPage;
    }

    public static void setNumberTrainOnPage(int numberTrainOnPage) {
        Util.numberTrainOnPage = numberTrainOnPage;
    }

    public static double getCoefficientByCarriageType(String carriageType) {
        return coefficientsByCarriageTypes.get(carriageType);
    }

    public static void addCoefficientByCarriageType(String carriageType, double coefficient) {
        coefficientsByCarriageTypes.put(carriageType, coefficient);
    }
}
