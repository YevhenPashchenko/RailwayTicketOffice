package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.entity.Train;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class that creates a list of dates for a new train schedule
 *
 * @author Yevhen Pashchenko
 */
public class TrainScheduleService implements ScheduleService {

    @Override
    public List<String> create(Train train) {
        List<String> scheduleDates = new ArrayList<>();
        int scheduleDurationForTrain;
        LocalTime localTimeNow = LocalTime.now();
        LocalDate date = LocalDate.now();
        if (localTimeNow.isBefore(train.getDepartureTime())) {
            scheduleDurationForTrain = Util.getScheduleDuration();
        } else {
            scheduleDurationForTrain = Util.getScheduleDuration() - 1;
            date = date.plusDays(1);
        }
        for (int i = 0; i < scheduleDurationForTrain; i++) {
            String currentDate = date.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
            scheduleDates.add(currentDate);
        }
        return scheduleDates;
    }
}
