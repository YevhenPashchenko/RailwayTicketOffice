package com.my.railwayticketoffice.entity;

import com.my.railwayticketoffice.DayOfWeekLocaleUA;
import com.my.railwayticketoffice.Util;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for methods from {@link Train}.
 *
 * @author Yevhen Pashchenko
 */
public class TrainTest {

    /**
     * Test for method checkDirectionIsRight from {@link Train}.
     */
    @Test
    public void testCheckDirectionIsRight() {

        Train train = new Train();
        train.getRoute().addDistanceFromStart(1, 0);
        train.getRoute().addDistanceFromStart(2, 200);
        train.getRoute().addDistanceFromStart(3, 0);

        assertTrue(train.getRoute().checkDirectionIsRight(1, 2));
        assertFalse(train.getRoute().checkDirectionIsRight(1, 3));
        assertFalse(train.getRoute().checkDirectionIsRight(3, 1));
    }

    /**
     * Test for method getDepartureDayOfWeekAndDateAsString from {@link Train}.
     */
    @Test
    public void testGetDepartureDayOfWeekAndDateAsString() {

        LocalDate date = LocalDate.now();

        Train train = new Train();

        String dayOfWeekEn = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String dayOfWeekUA = DayOfWeekLocaleUA.of(date.getDayOfWeek().getValue());
        String dateAsString = date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH));

        assertEquals(dayOfWeekEn + ", " + dateAsString, train.getRoute().getDepartureDayOfWeekAndDateAsString(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), "en"));
        assertEquals(dayOfWeekUA + ", " + dateAsString, train.getRoute().getDepartureDayOfWeekAndDateAsString(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), "uk"));

    }

    /**
     * Test for method getDestinationDayOfWeekAndDateAsString from {@link Train}.
     */
    @Test
    public void testGetDestinationDayOfWeekAndDateAsString() {

        LocalDate date = LocalDate.now();

        Train train = new Train();
        train.setDepartureTime(LocalTime.parse("00:00"));
        train.getRoute().addTimeSinceStart(1, "00:00");
        train.getRoute().addTimeSinceStart(2, "01:00");
        train.getRoute().addTimeSinceStart(3, "13:00");
        train.getRoute().addTimeSinceStart(4, "25:00");
        train.getRoute().addTimeSinceStart(5, "37:00");
        train.getRoute().addTimeSinceStart(6, "49:00");

        String dayOfWeekEn;
        String dayOfWeekUA;
        String dateAsString;
        int count = 2;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                dayOfWeekEn = date.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                dayOfWeekUA = DayOfWeekLocaleUA.of(date.plusDays(i).getDayOfWeek().getValue());
                dateAsString = date.plusDays(i).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH));

                assertEquals(dayOfWeekEn + ", " + dateAsString, train.getRoute().getDestinationDayOfWeekAndDateAsString(1, count + j, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), "en"));
                assertEquals(dayOfWeekUA + ", " + dateAsString, train.getRoute().getDestinationDayOfWeekAndDateAsString(1, count + j, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), "uk"));
            }
            count = count + 2;
        }

        dayOfWeekEn = date.plusDays(2).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        dayOfWeekUA = DayOfWeekLocaleUA.of(date.plusDays(2).getDayOfWeek().getValue());
        dateAsString = date.plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH));

        assertEquals(dayOfWeekEn + ", " + dateAsString, train.getRoute().getDestinationDayOfWeekAndDateAsString(1, 6, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), "en"));
        assertEquals(dayOfWeekUA + ", " + dateAsString, train.getRoute().getDestinationDayOfWeekAndDateAsString(1, 6, date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), "uk"));

    }

    /**
     * Test for method getArrivalTime from {@link Train}.
     */
    @Test
    public void testGetArrivalTime() {

        Train train = new Train();
        train.setDepartureTime(LocalTime.parse("00:10"));
        train.getRoute().addTimeSinceStart(1, "01:20");

        assertEquals("01:30", train.getRoute().getArrivalTime(1));

    }

    /**
     * Test for method getDurationTrip from {@link Train}.
     */
    @Test
    public void testGetDurationTrip() {

        Train train = new Train();
        train.setDepartureTime(LocalTime.parse("00:10"));
        train.getRoute().addTimeSinceStart(1, "01:20");
        train.getRoute().addTimeSinceStart(2, "02:50");

        assertEquals("01:30", train.getRoute().getDurationTrip(1, 2));

    }

    /**
     * Test for method getCostOfTripAsString from {@link Train}.
     */
    @Test
    public void testGetCostOfTripAsString() {

        Train train = new Train();
        for (int i = 1; i < 8; i++) {
            train.getRoute().addDistanceFromStart(i, i * 150);
        }

        for (int i = 2; i < 8; i++) {
            int tripDistance = train.getRoute().getDistanceFromFirstStation(i) - train.getRoute().getDistanceFromFirstStation(1);
            double costOfTrip = Util.getBasicTicketCost() + tripDistance * Util.getOneKilometerRoadCost() * (1 - getCoefficientDependingOnDistanceOfTrip(tripDistance));

            assertEquals(new DecimalFormat("#0.00").format(costOfTrip), train.getRoute().getCostOfTripAsString(1, i));
        }
    }

    private double getCoefficientDependingOnDistanceOfTrip(int tripDistance) {
        double coefficient = BigDecimal.valueOf((double) tripDistance / 1000).setScale(1, RoundingMode.FLOOR).doubleValue();
        if (coefficient > 0.5) {
            coefficient = 0.5;
        }
        return coefficient;
    }
}
