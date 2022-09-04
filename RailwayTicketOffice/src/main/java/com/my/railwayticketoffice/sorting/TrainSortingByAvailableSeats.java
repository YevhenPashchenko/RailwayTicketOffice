package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that sorting list of {@link Train} by available seats
 */
public class TrainSortingByAvailableSeats implements TrainSorting {

    @Override
    public List<Train> sort(List<Train> trains, Map<String, String> parameters) {
        return trains.stream().sorted(Comparator.comparingInt(Train::getSeats).reversed()).collect(Collectors.toList());
    }
}
