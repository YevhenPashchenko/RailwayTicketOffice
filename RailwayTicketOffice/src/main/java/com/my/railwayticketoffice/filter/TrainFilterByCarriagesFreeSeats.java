package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that filter list of {@link Train} by free seats in carriages
 *
 * @author Yevhen Pashchenko
 */
public class TrainFilterByCarriagesFreeSeats implements TrainFilter {

    @Override
    public List<Train> filter(List<Train> trains, Map<String, String> parameters) {
        for (Train train:
             trains) {
            Map<Integer, Train.Carriage> filteredCarriages = train.getCarriages().values().stream()
                    .filter(carriage -> carriage.getSeats().size() > 0)
                    .collect(Collectors.toMap(Train.Carriage::getId, carriage -> carriage));
            train.setCarriages(filteredCarriages);
        }
        return trains;
    }
}
