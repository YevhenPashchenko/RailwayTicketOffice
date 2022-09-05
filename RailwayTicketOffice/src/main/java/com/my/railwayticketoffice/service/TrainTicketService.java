package com.my.railwayticketoffice.service;

import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that creates a list of {@link Ticket}
 *
 * @author Yevhen Pashchenko
 */
public class TrainTicketService implements TicketService {

    @Override
    public List<Ticket> create(Train train, int maxTrainSeats, Map<String, String> parameters, Map<String, String[]> passengersData) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < passengersData.get("surname").length; i++) {
            Ticket ticket = new Ticket();
            ticket.setTrainNumber(train.getNumber());
            ticket.setSeatNumber(train.getSeatNumber(maxTrainSeats));
            train.setSeats(train.getSeats() - 1);

            ticket.setDepartureStation(train.getRoute().getStationNameByStationId(Integer.parseInt(parameters.get("from"))));
            ticket.setDestinationStation(train.getRoute().getStationNameByStationId(Integer.parseInt(parameters.get("to"))));

            ticket.setDepartureDateTime(train.getRoute().getDepartureDateTime(Integer.parseInt(parameters.get("from")), parameters.get("date")));
            ticket.setDestinationDateTime(train.getRoute().getDestinationDateTime(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), parameters.get("date")));

            ticket.setPassengerSurname(passengersData.get("surname")[i]);
            ticket.setPassengerName(passengersData.get("name")[i]);

            ticket.setCost(train.getRoute().getCostOfTripAsString(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to"))));
            ticket.setTicketNumber();
            tickets.add(ticket);
        }
        return tickets;
    }
}
