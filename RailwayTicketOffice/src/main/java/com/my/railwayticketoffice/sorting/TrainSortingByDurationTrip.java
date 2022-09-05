package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDateTime;
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
                    LocalDateTime durationTrip1 = getDuration(train1, parameters);
                    LocalDateTime durationTrip2 = getDuration(train2, parameters);
                    return durationTrip1.compareTo(durationTrip2);
                })
                .collect(Collectors.toList());
    }

    private LocalDateTime getDuration(Train train, Map<String, String> parameters) {
        String[] stringDuration = train.getRoute().getDurationTrip(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), null)
                .split(":");
        LocalDateTime duration = LocalDateTime.MIN;
        if (stringDuration[0].length() > 2) {
            duration = duration.plusDays(Long.parseLong(stringDuration[0].split(" ")[0]));
        }
        return duration.plusHours(Long.parseLong(stringDuration[0].substring(stringDuration[0].length() - 2)))
                .plusMinutes(Long.parseLong(stringDuration[1]));
    }
}
