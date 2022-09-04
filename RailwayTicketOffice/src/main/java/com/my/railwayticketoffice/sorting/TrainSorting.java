package com.my.railwayticketoffice.sorting;

import com.my.railwayticketoffice.entity.Train;

import java.util.List;
import java.util.Map;

/**
 * Interface that provides a method that sorting {@link com.my.railwayticketoffice.entity.Train}
 *
 * @author Yevhen Pashchenko
 */
public interface TrainSorting {

    List<Train> sort(List<Train> trains, Map<String, String> parameters);
}
