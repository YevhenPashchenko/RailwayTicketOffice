package com.my.railwayticketoffice.entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Train implements Serializable {

    /**Key of outer Map is the distance from the first station to the current station,
    key next Map is the time elapsed from departure from the first station to arrival at the current station
    and value last Map is a train stop time at the current station*/
    private Map<Integer, Map<LocalTime, Map<Station, LocalTime>>> route = new TreeMap<>();

    private int id;
    private String number;
    private int seats;
    private LocalTime departureTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public Map<Integer, Map<LocalTime, Map<Station, LocalTime>>> getRoute() {
        return route;
    }

    public void setRoute(Map<Integer, Map<LocalTime, Map<Station, LocalTime>>> route) {
        this.route = route;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return id == train.id && seats == train.seats && Objects.equals(number, train.number) && Objects.equals(departureTime, train.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, seats, departureTime);
    }
}
