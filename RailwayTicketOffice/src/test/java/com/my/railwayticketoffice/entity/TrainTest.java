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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for methods from {@link Train}.
 *
 * @author Yevhen Pashchenko
 */
public class TrainTest {

    /**
     * Test for method getCarriagesTypesNumber from {@link Train}.
     */
    @Test
    void testGetCarriagesTypesNumber() {
        Train train = new Train();

        String[] carriagesTypes = new String[] {"Л", "К", "П", "С1", "С2"};
        for (int i = 0; i < carriagesTypes.length; i++) {
            Train.Carriage carriage = train.new Carriage();
            carriage.setType(carriagesTypes[i]);
            train.addCarriage(i + 1, carriage);
        }

        assertEquals(5, train.getCarriagesTypesNumber());
        assertNotEquals(4, train.getCarriagesTypesNumber());
    }

    /**
     * Test for method getCarriageTypeOrderByMaxSeats from {@link Train}.
     */
    @Test
    void testGetCarriageTypeOrderByMaxSeats() {
        Train train = new Train();

        String[] carriagesTypes = new String[] {"Л", "К", "П", "С1", "С2"};
        int[] maxSeats = new int[] {20, 50, 10, 40, 30};

        for (int i = 0; i < carriagesTypes.length; i++) {
            Train.Carriage carriage = train.new Carriage();
            carriage.setType(carriagesTypes[i]);
            carriage.setMaxSeats(maxSeats[i]);
            train.addCarriage(i + 1, carriage);
        }

        assertEquals("П", train.getCarriageTypeOrderByMaxSeats(1));
        assertEquals("Л", train.getCarriageTypeOrderByMaxSeats(2));
        assertEquals("С2", train.getCarriageTypeOrderByMaxSeats(3));
        assertEquals("С1", train.getCarriageTypeOrderByMaxSeats(4));
        assertEquals("К", train.getCarriageTypeOrderByMaxSeats(5));
    }

    /**
     * Test for method getFreeSeatsSumByCarriageType from {@link Train}.
     */
    @Test
    void testGetFreeSeatsSumByCarriageType() {
        Train train = new Train();

        String[] carriagesTypes = new String[] {"Л", "К", "П", "П", "К"};
        int[] maxSeats = new int[] {20, 50, 40, 40, 50};

        for (int i = 0; i < carriagesTypes.length; i++) {
            Train.Carriage carriage = train.new Carriage();
            carriage.setType(carriagesTypes[i]);
            carriage.setMaxSeats(maxSeats[i]);
            for (int j = 0; j <= i; j++) {
                carriage.addSeat(j + 1);
            }
            train.addCarriage(i + 1, carriage);
        }

        assertEquals(1, train.getFreeSeatsSumByCarriageType(1));
        assertEquals(7, train.getFreeSeatsSumByCarriageType(2));
        assertEquals(7, train.getFreeSeatsSumByCarriageType(3));
    }

    /**
     * Test for method getCarriagesFilteredByTypeAndSortedByNumber from {@link Train}.
     */
    @Test
    void testGetCarriagesFilteredByTypeAndSortedByNumber() {
        Train train = new Train();

        String[] carriagesTypes = new String[] {"Л", "К", "П", "П", "К"};
        int[] carriagesNumbers = new int[] {2, 5, 3, 4, 1};

        for (int i = 0; i < carriagesTypes.length; i++) {
            Train.Carriage carriage = train.new Carriage();
            carriage.setType(carriagesTypes[i]);
            carriage.setNumber(carriagesNumbers[i]);
            train.addCarriage(i + 1, carriage);
        }

        Map<Integer, Train.Carriage> expected1 = new HashMap<>();
        expected1.put(train.getCarriages().get(5).getNumber(), train.getCarriages().get(5));
        expected1.put(train.getCarriages().get(2).getNumber(), train.getCarriages().get(2));

        Map<Integer, Train.Carriage> expected2 = new HashMap<>();
        expected2.put(train.getCarriages().get(3).getNumber(), train.getCarriages().get(3));
        expected2.put(train.getCarriages().get(4).getNumber(), train.getCarriages().get(4));

        assertEquals(expected1, train.getCarriagesFilteredByTypeAndSortedByNumber("К"));
        assertEquals(expected2, train.getCarriagesFilteredByTypeAndSortedByNumber("П"));
    }

    /**
     * Test for method isTrainHasBothStation from {@link Train}.
     */
    @Test
    void testIsTrainHasBothStation() {
        Train train1 = new Train();
        Train train2 = new Train();
        Station station1 = new Station();
        station1.setId(1);
        Station station2 = new Station();
        station2.setId(2);
        train1.getRoute().addStation(station1);
        train1.getRoute().addStation(station2);
        train2.getRoute().addStation(station1);

        assertTrue(train1.getRoute().isTrainHasBothStation(station1.getId(), station2.getId()));
        assertFalse(train2.getRoute().isTrainHasBothStation(station1.getId(), station2.getId()));
    }

    /**
     * Test for method checkDirectionIsRight from {@link Train}.
     */
    @Test
    void testCheckDirectionIsRight() {

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
    void testGetDepartureDayOfWeekAndDateAsString() {

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
    void testGetDestinationDayOfWeekAndDateAsString() {

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
    void testGetArrivalTime() {

        Train train = new Train();
        train.setDepartureTime(LocalTime.parse("00:10"));
        train.getRoute().addTimeSinceStart(1, "01:20");

        assertEquals("01:30", train.getRoute().getArrivalTime(1));

    }

    /**
     * Test for method getDurationTrip from {@link Train}.
     */
    @Test
    void testGetDurationTrip() {

        Train train = new Train();
        train.setDepartureTime(LocalTime.parse("00:10"));
        train.getRoute().addTimeSinceStart(1, "01:20");
        train.getRoute().addTimeSinceStart(2, "02:50");

        assertEquals("01:30", train.getRoute().getDurationTrip(1, 2, null));

        train.getRoute().addTimeSinceStart(1, "01:20");
        train.getRoute().addTimeSinceStart(2, "25:50");

        assertEquals("1 d. 00:30", train.getRoute().getDurationTrip(1, 2, "en"));
        assertEquals("1 д. 00:30", train.getRoute().getDurationTrip(1, 2, "uk"));

    }

    /**
     * Test for method getCostOfTripAsString from {@link Train}.
     */
    @Test
    void testGetCostOfTripAsString() {

        Train train = new Train();
        for (int i = 1; i < 8; i++) {
            train.getRoute().addDistanceFromStart(i, i * 150);
        }

        for (int i = 2; i < 8; i++) {
            int tripDistance = train.getRoute().getDistanceFromFirstStation(i) - train.getRoute().getDistanceFromFirstStation(1);
            double costOfTrip = (Util.getBasicTicketCost() + tripDistance * Util.getOneKilometerRoadCost() * (1 - getCoefficientDependingOnDistanceOfTrip(tripDistance))) * Util.getCoefficientByCarriageType("Л");

            assertEquals(new DecimalFormat("#0.00").format(costOfTrip), train.getRoute().getCostOfTripAsString(1, i, "Л"));
        }
    }

    private double getCoefficientDependingOnDistanceOfTrip(int tripDistance) {
        double coefficient = BigDecimal.valueOf((double) tripDistance / 1000).setScale(1, RoundingMode.FLOOR).doubleValue();
        if (coefficient > 0.5) {
            coefficient = 0.5;
        }
        return coefficient;
    }

    /**
     * Test for method checkIfStationIsOnTheRoute from {@link Train}.
     */
    @Test
    void testCheckIfStationIsOnTheRoute() {
        Train train = new Train();
        Station station = new Station();
        station.setId(1);
        train.getRoute().addStation(station);

        Station station1 = new Station();
        station1.setId(2);

        assertTrue(train.getRoute().checkIfStationIsOnTheRoute(station.getId()));
        assertFalse(train.getRoute().checkIfStationIsOnTheRoute(station1.getId()));
    }

    /**
     * Test for method checkIfNewTimeSinceStartCorrect from {@link Train}.
     */
    @Test
    void testCheckIfNewTimeSinceStartCorrect() {
        Train train = new Train();

        Station station = new Station();
        station.setId(1);
        Station station1 = new Station();
        station1.setId(2);

        train.getRoute().addStation(station);
        train.getRoute().addStation(station1);

        train.getRoute().addTimeSinceStart(station.getId(), "00:10");
        train.getRoute().addTimeSinceStart(station1.getId(), "00:20");

        assertTrue(train.getRoute().checkIfNewTimeSinceStartCorrect(station.getId(), "00:05"));
        assertFalse(train.getRoute().checkIfNewTimeSinceStartCorrect(station.getId(), "00:25"));

        assertTrue(train.getRoute().checkIfNewTimeSinceStartCorrect(station1.getId(), "00:25"));
        assertFalse(train.getRoute().checkIfNewTimeSinceStartCorrect(station1.getId(), "00:05"));

        Station station2 = new Station();
        station1.setId(3);

        train.getRoute().addStation(station2);

        train.getRoute().addTimeSinceStart(station2.getId(), "00:30");

        assertTrue(train.getRoute().checkIfNewTimeSinceStartCorrect(station1.getId(), "00:25"));
        assertFalse(train.getRoute().checkIfNewTimeSinceStartCorrect(station1.getId(), "00:05"));
        assertFalse(train.getRoute().checkIfNewTimeSinceStartCorrect(station1.getId(), "00:35"));
    }

    /**
     * Test for method checkIfNewDistanceFromStartCorrect from {@link Train}.
     */
    @Test
    public void testCheckIfNewDistanceFromStartCorrect() {
        Train train = new Train();

        Station station = new Station();
        station.setId(1);
        Station station1 = new Station();
        station1.setId(2);

        train.getRoute().addStation(station);
        train.getRoute().addStation(station1);

        train.getRoute().addDistanceFromStart(station.getId(), 10);
        train.getRoute().addDistanceFromStart(station1.getId(), 20);

        assertTrue(train.getRoute().checkIfNewDistanceFromStartCorrect(station.getId(), 5));
        assertFalse(train.getRoute().checkIfNewDistanceFromStartCorrect(station.getId(), 25));

        assertTrue(train.getRoute().checkIfNewDistanceFromStartCorrect(station1.getId(), 25));
        assertFalse(train.getRoute().checkIfNewDistanceFromStartCorrect(station1.getId(), 5));

        Station station2 = new Station();
        station1.setId(3);

        train.getRoute().addStation(station2);

        train.getRoute().addDistanceFromStart(station2.getId(), 30);

        assertTrue(train.getRoute().checkIfNewDistanceFromStartCorrect(station1.getId(), 25));
        assertFalse(train.getRoute().checkIfNewDistanceFromStartCorrect(station1.getId(), 5));
        assertFalse(train.getRoute().checkIfNewDistanceFromStartCorrect(station1.getId(), 35));
    }
}
