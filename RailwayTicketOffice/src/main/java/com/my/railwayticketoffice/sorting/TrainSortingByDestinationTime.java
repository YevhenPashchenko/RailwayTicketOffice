package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that sorting list of {@link Train} by destination time
 *
 * @author Yevhen Pashchenko
 */
public class TrainSortingByDestinationTime implements TrainSorting {

    @Override
    public List<Train> sort(List<Train> trains, Map<String, String> parameters) {
        return trains.stream()
                .sorted(Comparator.comparing(train -> train.getRoute().getDestinationDateTime(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), parameters.get("date"))))
                .collect(Collectors.toList());
    }
}
