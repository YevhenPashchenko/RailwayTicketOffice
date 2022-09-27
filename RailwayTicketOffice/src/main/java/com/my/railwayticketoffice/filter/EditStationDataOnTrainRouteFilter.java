package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that filter list of {@link Train} according to the correctness of the parameters when editing
 * station data on the route
 *
 * @author Yevhen Pashchenko
 */
public class EditStationDataOnTrainRouteFilter implements TrainFilter {

    @Override
    public List<Train> filter(List<Train> trains, Map<String, String> parameters) {
        List<Train> filteredTrains = new ArrayList<>();
        int stationId = Integer.parseInt(parameters.get("stationId"));
        for (Train train:
             trains) {
            if (!train.getRoute().checkIfStationIsOnTheRoute(stationId)) {
                break;
            }
            if (!parameters.get("timeSinceStart").equals(train.getRoute().getTimeSinceStart(stationId))) {
                if (!train.getRoute().checkIfNewTimeSinceStartCorrect(stationId, parameters.get("timeSinceStart"))) {
                    break;
                }
            }
            if (Integer.parseInt(parameters.get("distanceFromStart")) != train.getRoute().getDistanceFromFirstStation(stationId)) {
                if (!train.getRoute().checkIfNewDistanceFromStartCorrect(stationId, Integer.parseInt(parameters.get("distanceFromStart")))) {
                    break;
                }
            }
            filteredTrains.add(train);
        }
        return filteredTrains;
    }
}
