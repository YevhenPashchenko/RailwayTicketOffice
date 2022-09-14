package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.Train;

import java.util.List;
import java.util.Map;

/**
 * Interface that provides a method that creates a list of {@link com.my.railwayticketoffice.entity.Ticket}
 */
public interface TicketService {

    /**
     * Method that creates a list of {@link com.my.railwayticketoffice.entity.Ticket}.
     * @param train {@link Train}.
     * @param ticketParameters map of ticket parameters.
     * @return list of {@link com.my.railwayticketoffice.entity.Ticket}.
     */
    List<Ticket> create(Train train, Map<String, String> parameters, Map<String, String[]> ticketParameters);
}
