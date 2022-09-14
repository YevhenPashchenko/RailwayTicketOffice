package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.entity.Train;

import java.util.List;
import java.util.Map;

/**
 * Interface that provides a methods that prepare a data for managing schedule
 *
 * @author Yevhen Pashchenko
 */
public interface ScheduleService {

    List<String> create();

    Map<Integer, List<Integer>> collect(Train train, Map<String, String[]> ticketParameters);
}
