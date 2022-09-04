package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that sorting list of {@link Train} by departure time
 *
 * @author Yevhen Pashchenko
 */
public class TrainSortingByDepartureTime implements TrainSorting {

    @Override
    public List<Train> sort(List<Train> trains, Map<String, String> parameters) {
        return trains.stream().sorted((train1, train2) -> {
            LocalTime departureTime1 = getDepartureTime(train1, parameters);
            LocalTime departureTime2 = getDepartureTime(train2, parameters);
            return departureTime1.compareTo(departureTime2);
        }).collect(Collectors.toList());
    }

    private LocalTime getDepartureTime(Train train, Map<String, String> parameters) {
        return train.getDepartureTime()
                .plusHours(Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("from"))).split(":")[0]))
                .plusMinutes(Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("from"))).split(":")[1]));
    }
}
