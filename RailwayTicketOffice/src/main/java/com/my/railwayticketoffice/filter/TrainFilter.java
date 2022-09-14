package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Interface that provides a method that filter trains.
 *
 * @author Yevhen Pashchenko
 */
public interface TrainFilter {

    /**
     * Method that filter trains.
     * @param trains - list of {@link Train}.
     * @return - list of filtered {@link Train}.
     */
    List<Train> filter(List<Train> trains, Map<String, String> parameters);
}
