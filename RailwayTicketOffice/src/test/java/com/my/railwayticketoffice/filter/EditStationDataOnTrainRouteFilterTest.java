package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link EditStationDataOnTrainRouteFilter}
 *
 * @author Yevhen Pashchenko
 */
public class EditStationDataOnTrainRouteFilterTest {

    /**
     * Test for method filter from {@link EditStationDataOnTrainRouteFilter}.
     */
    @Test
    public void testFilter() {
        Train train = new Train();

        Station station = new Station();
        station.setId(1);
        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), "00:10");
        train.getRoute().addDistanceFromStart(station.getId(), 10);

        Map<String, String> parameters = new HashMap<>();

        parameters.put("stationId", "2");
        assertEquals(0, new EditStationDataOnTrainRouteFilter().filter(Collections.singletonList(train), parameters).size());

        Station station1 = new Station();
        station1.setId(2);
        train.getRoute().addStation(station1);
        train.getRoute().addTimeSinceStart(station1.getId(), "00:20");

        parameters.put("stationId", station.getId() + "");
        parameters.put("timeSinceStart", "00:25");
        assertEquals(0, new EditStationDataOnTrainRouteFilter().filter(Collections.singletonList(train), parameters).size());

        train.getRoute().addDistanceFromStart(station1.getId(), 20);

        parameters.put("stationId", station.getId() + "");
        parameters.put("timeSinceStart", "00:05");
        parameters.put("distanceFromStart", "25");
        assertEquals(0, new EditStationDataOnTrainRouteFilter().filter(Collections.singletonList(train), parameters).size());

        parameters.put("distanceFromStart", "5");
        assertEquals(1, new EditStationDataOnTrainRouteFilter().filter(Collections.singletonList(train), parameters).size());
    }
}
