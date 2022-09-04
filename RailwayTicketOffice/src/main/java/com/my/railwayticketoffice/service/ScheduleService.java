package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.entity.Train;

import java.util.List;

/**
 * Interface that provides a method that creates a list of dates for a new train schedule
 */
public interface ScheduleService {

    /**
     * Creates a list of dates for a new train schedule.
     * @param train - {@link Train}.
     * @return list of dates for a new train schedule.
     */
    List<String> create(Train train);
}
