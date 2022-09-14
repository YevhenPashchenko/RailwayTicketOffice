package com.my.railwayticketoffice.sorting;

import java.util.HashMap;
import java.util.Map;

/**
 *  Class that manages how to sort trains.
 *
 *  @author Yevhen Pashchenko
 */
public class TrainSortingManger {

    private static final Map<String, TrainSorting> SORTING_TYPES;

    static {
        SORTING_TYPES = new HashMap<>();

        SORTING_TYPES.put("departureTime", new TrainSortingByDepartureTime());
        SORTING_TYPES.put("destinationTime", new TrainSortingByDestinationTime());
        SORTING_TYPES.put("durationTrip", new TrainSortingByDurationTrip());
    }

    public static TrainSorting getStrategy(String sortBy) {
        return SORTING_TYPES.get(sortBy);
    }
}
