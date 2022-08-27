package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that filter list of {@link Train} by direction and departure time.
 */
public class TrainFilterByDirectionAndDepartureTime implements TrainFilter {

    @Override
    public List<Train> filter(List<Train> trains, int departureStationId, int destinationStationId, LocalDate departureDate) {
        return trains.stream()
                .filter(train -> train.getRoute().checkDirectionIsRight(departureStationId, destinationStationId))
                .filter(train -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter);
                    if (currentDateTime.getDayOfMonth() == departureDate.getDayOfMonth()) {
                        LocalDateTime beginningNextDayFromSearchDate = departureDate.plusDays(1).atStartOfDay();
                        LocalDateTime departureDateTimeFromDepartureStationAtSearchDate = departureDate.atTime(train.getDepartureTime())
                                .plusHours(Long.parseLong(train.getRoute().getTimeSinceStart(departureStationId).split(":")[0]))
                                .plusMinutes(Long.parseLong(train.getRoute().getTimeSinceStart(departureStationId).split(":")[1]));
                        return departureDateTimeFromDepartureStationAtSearchDate.compareTo(currentDateTime) > 0 && departureDateTimeFromDepartureStationAtSearchDate.compareTo(beginningNextDayFromSearchDate) < 0;
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }
}
