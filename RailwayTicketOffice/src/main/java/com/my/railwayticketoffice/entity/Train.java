package com.my.railwayticketoffice.entity;

import com.my.railwayticketoffice.DayOfWeekLocaleUA;
import com.my.railwayticketoffice.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class Train implements Serializable {

    private Route route = new Route();
    private int id;
    private String number;
    private int seats;
    private LocalTime departureTime;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

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

    public class Route {

        private final List<Station> stations = new ArrayList<>();
        /**
         * Key - station id, value - the time elapsed from departure from the first station to the current station.
         */
        private final Map<Integer, LocalTime> timeSinceStartMap = new HashMap<>();
        /**
         * Key - station id, value - the train stop time on current station.
         */
        private final Map<Integer, LocalTime> stopTimeMap = new HashMap<>();
        /**
         * Key - station id, value - distance from the first station to the current station.
         */
        private final Map<Integer, Integer> distanceFromStartMap = new HashMap<>();

        /**
         * Checks that the train goes in the needed direction.
         * @param fromStationId - departure station id.
         * @param toStationId - destination station id.
         * @return true if difference between distance to destination station and distance to departure station
         * has positive value.
         */
        public boolean checkDirectionIsRight(int fromStationId, int toStationId) {
            return distanceFromStartMap.get(toStationId) - distanceFromStartMap.get(fromStationId) > 0;
        }

        /**
         *
         * @return name of the first station of the train route.
         */
        public String getDepartureStationName() {
            return stations.get(0).getName();
        }

        /**
         *
         * @return name of the last station of the train route.
         */
        public String getDestinationStationName() {
            return stations.get(stations.size() - 1).getName();
        }

        /**
         *
         * @param stationId - departure or destination station id.
         * @param departureDate - departure {@link LocalDate}.
         * @return date of week and date when train departs from the departure station
         * or arrives at the destination station as string.
         */
        public String getDateOfWeekAndDateAsString(int stationId, LocalDate departureDate) {
            LocalDateTime departureDateFromDepartureStation;
            LocalTime timeSinceDepartureFromFirstStation = timeSinceStartMap.get(stationId);
            departureDateFromDepartureStation = departureDate.atTime(departureTime).plusNanos(timeSinceDepartureFromFirstStation.toNanoOfDay());
            return DayOfWeekLocaleUA.of(departureDateFromDepartureStation.getDayOfWeek().getValue()).toString() +
                    ", " + departureDateFromDepartureStation.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH));
        }

        public String getArrivalTime(int stationId) {
            return departureTime.plusNanos(timeSinceStartMap.get(stationId).toNanoOfDay()).toString();
        }

        public String getDurationTrip(int fromStationId, int toStationId) {
            return departureTime.plusNanos(timeSinceStartMap.get(toStationId).toNanoOfDay())
                    .minusNanos(departureTime.plusNanos(timeSinceStartMap.get(fromStationId).toNanoOfDay()).toNanoOfDay()).toString();
        }

        public int getDistanceFromFirstStation(int stationId) {
            return distanceFromStartMap.get(stationId);
        }

        public int getTimeStopAtStationInMinutes(int stationId) {
            return stopTimeMap.get(stationId).getMinute();
        }

        public String getCostOfTripAsString(int fromStationId, int toStationId) {
            int tripDistance = distanceFromStartMap.get(toStationId) - distanceFromStartMap.get(fromStationId);
            double costOfTrip = Util.getBasicTicketCost() + tripDistance * Util.getOneKilometerRoadCost() * (1 - getCoefficientDependingOnDistanceOfTrip(tripDistance));
            return new DecimalFormat("#0.00").format(costOfTrip);
        }

        private double getCoefficientDependingOnDistanceOfTrip(int tripDistance) {
            double coefficient = BigDecimal.valueOf((double) tripDistance / 1000).setScale(1, RoundingMode.FLOOR).doubleValue();
            if (coefficient > 0.5) {
                coefficient = 0.5;
            }
            return coefficient;
        }

        public String getStationNameByStationId(int stationId) {
            String stationName = "";
            for (Station station:
                 stations) {
                if (station.getId() == stationId) {
                    stationName = station.getName();
                    break;
                }
            }
            return stationName;
        }

        public List<Station> getStations() {
            return stations;
        }

        public LocalTime getTimeSinceStart(Integer stationId) {
            return timeSinceStartMap.get(stationId);
        }

        public LocalTime getStopTime(Integer stationId) {
            return stopTimeMap.get(stationId);
        }

        public void addStation(Station station) {
            stations.add(station);
        }

        public void addTimeSinceStart(Integer stationId, LocalTime timeSinceStart) {
            timeSinceStartMap.put(stationId, timeSinceStart);
        }

        public void addStopTime(Integer stationId, LocalTime stopTime) {
            stopTimeMap.put(stationId, stopTime);
        }

        public void addDistanceFromStart(Integer stationId, Integer distanceFromStart) {
            distanceFromStartMap.put(stationId, distanceFromStart);
        }
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
