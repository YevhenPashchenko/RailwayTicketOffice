package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class that creates a data for manging a schedule
 *
 * @author Yevhen Pashchenko
 */
public class TrainScheduleService implements ScheduleService {

    /**
     * Creates a list of dates for a new train schedule.
     * @return list of dates for a new train schedule.
     */
    @Override
    public List<String> create() {
        List<String> scheduleDates = new ArrayList<>();
        LocalDate date = LocalDate.now();
        for (int i = 0; i < Util.getScheduleDuration(); i++) {
            String currentDate = date.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
            scheduleDates.add(currentDate);
        }
        return scheduleDates;
    }

    /**
     * Creates a map in which key is a carriage id and value is an id of seats that belongs this carriage for deleting them
     * from schedule.
     * @param train {@link Train}.
     * @param ticketParameters map that collect information about carriages and relative seats which were ordered and them must be deleted from schedule.
     * @return map in which key is a carriage id and value is an id of seats that belongs this carriage for deleting them from schedule.
     */
    @Override
    public Map<Integer, List<Integer>> collect(Train train, Map<String, String[]> ticketParameters) {
        String[] carriagesNumbers = ticketParameters.get("carriage");
        String[] carriagesSeats = ticketParameters.get("seat");
        Map<Integer, List<Integer>> seatsForDelete = new HashMap<>();
        for (int i = 0; i < carriagesNumbers.length; i++) {
            int finalI = i;
            int carriageId = train.getCarriages().values().stream()
                    .filter(carriage -> carriage.getNumber() == Integer.parseInt(carriagesNumbers[finalI]))
                    .findFirst().get().getId();
            List<Integer> seats = seatsForDelete.getOrDefault(carriageId, new ArrayList<>());
            seats.add(Integer.parseInt(carriagesSeats[i]));
            seatsForDelete.put(carriageId, seats);
        }
        return seatsForDelete;
    }
}
