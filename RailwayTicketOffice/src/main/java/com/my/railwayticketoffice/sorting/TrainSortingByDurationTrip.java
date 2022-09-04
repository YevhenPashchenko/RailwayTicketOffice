package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that sorting list of {@link Train} by duration trip
 *
 * @author Yevhen Pashchenko
 */
public class TrainSortingByDurationTrip implements TrainSorting {

    @Override
    public List<Train> sort(List<Train> trains, Map<String, String> parameters) {
        return trains.stream()
                .sorted((train1, train2) -> {
                    LocalTime durationTrip1 = LocalTime.parse(train1.getRoute().getDurationTrip(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to"))));
                    LocalTime durationTrip2 = LocalTime.parse(train2.getRoute().getDurationTrip(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to"))));
                    return durationTrip1.compareTo(durationTrip2);
                })
                .collect(Collectors.toList());
    }
}
