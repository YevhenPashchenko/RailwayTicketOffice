package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that filter list of {@link Train} by direction and departure time.
 *
 * @author Yevhen Pashchenko
 */
public class TrainFilterByDirectionAndDepartureTime implements TrainFilter {

    @Override
    public List<Train> filter(List<Train> trains, Map<String, String> parameters) {
        int departureStationId = Integer.parseInt(parameters.get("from"));
        int destinationStationId = Integer.parseInt(parameters.get("to"));
        List<String> date = Arrays.asList(parameters.get("date").split("\\."));
        Collections.reverse(date);
        LocalDate departureDate = LocalDate.parse(String.join("-", date));
        return trains.stream()
                .filter(train -> train.getRoute().isTrainHasBothStation(departureStationId, destinationStationId))
                .filter(train -> train.getRoute().checkDirectionIsRight(departureStationId, destinationStationId))
                .filter(train -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
                    if (currentDateTime.getDayOfMonth() == departureDate.getDayOfMonth()) {
                        return LocalTime.parse(train.getRoute().getArrivalTime(departureStationId)).compareTo(currentDateTime.toLocalTime()) > 0;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
