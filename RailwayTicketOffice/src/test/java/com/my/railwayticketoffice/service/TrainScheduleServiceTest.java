package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link TrainScheduleService}
 *
 * @author Yevhen Pashchenko
 */
public class TrainScheduleServiceTest {

    /**
     * Test for method create from {@link TrainScheduleService}.
     */
    @Test
    void testCreate() {
        ScheduleService trainScheduleService = new TrainScheduleService();
        List<String> scheduleDates = new ArrayList<>();
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();

        Train train = new Train();
        train.setDepartureTime(time.plusHours(1));

        for (int i = 0; i < Util.getScheduleDuration(); i++) {
            String currentDate = date.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
            scheduleDates.add(currentDate);
        }

        assertEquals(scheduleDates, trainScheduleService.create());
    }

    /**
     * Test for method collect from {@link TrainScheduleService}.
     */
    @Test
    void testCollect() {
        Train train = new Train();
        for (int i = 0; i < 3; i++) {
            Train.Carriage carriage = train.new Carriage();
            carriage.setId(i + 1);
            carriage.setNumber(i + 1);

        }
    }
}
