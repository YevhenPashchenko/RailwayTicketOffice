package com.my.railwayticketoffice.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Ticket implements Serializable {

    private String ticketNumber;
    private String trainNumber;
    private int carriageNumber;
    private String carriageType;
    private int seatNumber;
    private String departureStation;
    private String destinationStation;
    private LocalDateTime departureDateTime;
    private LocalDateTime destinationDateTime;
    private String passengerSurname;
    private String passengerName;
    private String cost;

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber() {
        String joinedDepartureDateTime = String.join("",  departureDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).replaceAll("[\\s:]", ".").split("\\."));
        String joinedDestinationDateTime = String.join("",  destinationDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")).replaceAll("[\\s:]", ".").split("\\."));
        ticketNumber = "" + trainNumber + carriageNumber + seatNumber + "-" + joinedDepartureDateTime + "-" + joinedDestinationDateTime;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public int getCarriageNumber() {
        return carriageNumber;
    }

    public void setCarriageNumber(int carriageNumber) {
        this.carriageNumber = carriageNumber;
    }

    public String getCarriageType() {
        return carriageType;
    }

    public void setCarriageType(String carriageType) {
        this.carriageType = carriageType;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(String departureStation) {
        this.departureStation = departureStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getDestinationDateTime() {
        return destinationDateTime;
    }

    public void setDestinationDateTime(LocalDateTime destinationDateTime) {
        this.destinationDateTime = destinationDateTime;
    }

    public String getPassengerSurname() {
        return passengerSurname;
    }

    public void setPassengerSurname(String passengerSurname) {
        this.passengerSurname = passengerSurname;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return carriageNumber == ticket.carriageNumber && seatNumber == ticket.seatNumber && Objects.equals(ticketNumber, ticket.ticketNumber) && Objects.equals(trainNumber, ticket.trainNumber) && Objects.equals(carriageType, ticket.carriageType) && Objects.equals(departureStation, ticket.departureStation) && Objects.equals(destinationStation, ticket.destinationStation) && Objects.equals(departureDateTime, ticket.departureDateTime) && Objects.equals(destinationDateTime, ticket.destinationDateTime) && Objects.equals(passengerSurname, ticket.passengerSurname) && Objects.equals(passengerName, ticket.passengerName) && Objects.equals(cost, ticket.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNumber, trainNumber, carriageNumber, carriageType, seatNumber, departureStation, destinationStation, departureDateTime, destinationDateTime, passengerSurname, passengerName, cost);
    }
}
