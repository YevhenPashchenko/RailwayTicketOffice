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
    public List<Ticket> create(Train train, Map<String, String> parameters, Map<String, String[]> ticketParameters) {
        String[] passengersSurnames = ticketParameters.get("surname");
        String[] passengersNames = ticketParameters.get("name");
        String[] carriagesNumbers = ticketParameters.get("carriage");
        String[] carriagesSeats = ticketParameters.get("seat");
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < ticketParameters.get("surname").length; i++) {
            Ticket ticket = new Ticket();

            ticket.setTrainNumber(train.getNumber());

            ticket.setCarriageNumber(Integer.parseInt(carriagesNumbers[i]));

            String carriageType = train.getCarriages().values().stream().filter(carriage -> carriage.getNumber() == ticket.getCarriageNumber()).findFirst().get().getType();
            ticket.setCarriageType(carriageType);

            ticket.setSeatNumber(Integer.parseInt(carriagesSeats[i]));

            ticket.setDepartureStation(train.getRoute().getStationNameByStationId(Integer.parseInt(parameters.get("from"))));
            ticket.setDestinationStation(train.getRoute().getStationNameByStationId(Integer.parseInt(parameters.get("to"))));

            ticket.setDepartureDateTime(train.getRoute().getDepartureDateTime(Integer.parseInt(parameters.get("from")), parameters.get("date")));
            ticket.setDestinationDateTime(train.getRoute().getDestinationDateTime(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), parameters.get("date")));

            ticket.setPassengerSurname(passengersSurnames[i]);
            ticket.setPassengerName(passengersNames[i]);

            ticket.setCost(train.getRoute().getCostOfTripAsString(Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), carriageType));
            ticket.setTicketNumber();
            tickets.add(ticket);
        }
        return tickets;
    }
}
