package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.util.Map;

/**
 * Interface that provides a method that check that the reserved seats on the train are not yet occupied.
 *
 * @author Yevhen Pashchenko
 */
public interface SeatFilter {

    /**
     * Check that the reserved seats on the train are not yet occupied.
     * @param train {@link Train}.
     * @param ticketParameters map that contains information about reserved seats.
     * @return true if reserved seats are free else false.
     */
    boolean check(Train train, Map<String, String[]> ticketParameters);
}
