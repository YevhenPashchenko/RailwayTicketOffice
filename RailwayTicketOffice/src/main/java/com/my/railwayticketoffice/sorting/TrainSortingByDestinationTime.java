package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
                .sorted((train1, train2) -> {
                    LocalDateTime destinationTime1 = getDestinationTime(train1, parameters);
                    LocalDateTime destinationTime2 = getDestinationTime(train2, parameters);
                    return destinationTime1.compareTo(destinationTime2);
                }).collect(Collectors.toList());
    }

    private LocalDateTime getDestinationTime(Train train, Map<String, String> parameters) {
        List<String> dateForDB = Arrays.asList(parameters.get("date").split("\\."));
        Collections.reverse(dateForDB);
        LocalDateTime destinationTime = LocalDate.parse(String.join("-", dateForDB))
                .atTime(train.getDepartureTime())
                .plusHours(Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("from"))).split(":")[0]))
                .plusMinutes(Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("from"))).split(":")[1]));
        if (destinationTime.getDayOfMonth() != LocalDate.parse(String.join("-", dateForDB)).getDayOfMonth()) {
            destinationTime = destinationTime.minusDays(destinationTime.getDayOfMonth() - LocalDate.parse(String.join("-", dateForDB)).getDayOfMonth());
        }
        destinationTime = destinationTime
                .plusHours(Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("to"))).split(":")[0]) - Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("from"))).split(":")[0]))
                .plusMinutes(Long.parseLong(train.getRoute().getTimeSinceStart(Integer.parseInt(parameters.get("to"))).split(":")[1]));
        return destinationTime;
    }
}
