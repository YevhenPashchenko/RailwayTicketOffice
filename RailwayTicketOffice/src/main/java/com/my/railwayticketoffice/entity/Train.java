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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Train implements Serializable {

    private Map<Integer, Carriage> carriages = new HashMap<>();
    private Route route = new Route();
    private int id;
    private String number;
    private LocalTime departureTime;

    /**
     * @return the number of types of carriages in train.
     */
    public int getCarriagesTypesNumber() {
        return (int) carriages.values().stream().map(Carriage::getType).distinct().count();
    }

    /**
     * @param count number by which the type of carriage is contains in the array sorted by maximum carriage seats.
     * @return carriage type.
     */
    public String getCarriageTypeOrderByMaxSeats(int count) {
        return (String) carriages.values().stream()
                .sorted(Comparator.comparingInt(Carriage::getMaxSeats))
                .map(Carriage::getType).distinct().toArray()[count - 1];
    }

    /**
     * @param count number for get type of carriage from method getCarriageTypeOrderByMaxSeats.
     * @return sum of free seats in carriages of the same type.
     */
    public int getFreeSeatsSumByCarriageType(int count) {
        String carriageType = getCarriageTypeOrderByMaxSeats(count);
        return carriages.values().stream()
                .filter(carriage -> carriageType.equals(carriage.type))
                .flatMapToInt(carriage -> IntStream.of(carriage.seats.size())).sum();
    }

    /**
     * @param carriageType chosen carriage type.
     * @return filtered by carriage type and sorted by carriage number map in which key is carriage number.
     */
    public Map<Integer, Carriage> getCarriagesFilteredByTypeAndSortedByNumber(String carriageType) {
        return carriages.values().stream()
                .filter(carriage -> carriage.type.equals(carriageType))
                .collect(Collectors.toMap(carriage -> carriage.number, carriage -> carriage));
    }

    public Map<Integer, Carriage> getCarriages() {
        return Collections.unmodifiableMap(carriages);
    }

    public void setCarriages(Map<Integer, Carriage> carriages) {
        this.carriages = carriages;
    }

    public void addCarriage(Integer carriageId, Carriage carriage) {
        carriages.put(carriageId, carriage);
    }

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

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public class Carriage implements Serializable {

        private int id;
        private int number;
        private String type;
        private int maxSeats;
        private final Set<Integer> seats = new TreeSet<>();

        public Set<Integer> getSeats() {
            return Collections.unmodifiableSet(seats);
        }

        public void addSeat(Integer seatNumber) {
            seats.add(seatNumber);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getMaxSeats() {
            return maxSeats;
        }

        public void setMaxSeats(int maxSeats) {
            this.maxSeats = maxSeats;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Carriage carriage = (Carriage) o;
            return id == carriage.id && number == carriage.number && maxSeats == carriage.maxSeats && Objects.equals(type, carriage.type) && Objects.equals(seats, carriage.seats);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, number, type, maxSeats, seats);
        }
    }

    public class Route implements Serializable {

        private final List<Station> stations = new ArrayList<>();
        /**
         * Key - station id, value - the time elapsed from departure from the first station to the current station.
         */
        private final Map<Integer, String> timeSinceStartMap = new HashMap<>();
        /**
         * Key - station id, value - the train stop time on current station.
         */
        private final Map<Integer, LocalTime> stopTimeMap = new HashMap<>();
        /**
         * Key - station id, value - distance from the first station to the current station.
         */
        private final Map<Integer, Integer> distanceFromStartMap = new HashMap<>();

        /**
         * Check that the train has both stations on the route.
         * @param fromStationId departure station id.
         * @param toStationId destination station id.
         * @return true if both stations contains in stations list or false if not.
         */
        public boolean isTrainHasBothStation(int fromStationId, int toStationId) {
            int isHas = 0;
            for (Station station:
                 stations) {
                if (station.getId() == fromStationId || station.getId() == toStationId) {
                    isHas++;
                    if (isHas == 2) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Checks that the train goes in the needed direction.
         * @param fromStationId departure station id.
         * @param toStationId destination station id.
         * @return true if difference between distance to destination station and distance to departure station
         * has positive value.
         */
        public boolean checkDirectionIsRight(int fromStationId, int toStationId) {
            return distanceFromStartMap.get(toStationId) - distanceFromStartMap.get(fromStationId) > 0;
        }

        /**
         * @return name of the first station of the train route.
         */
        public String getDepartureStationName() {
            return stations.get(0).getName();
        }

        /**
         * @return name of the last station of the train route.
         */
        public String getDestinationStationName() {
            return stations.get(stations.size() - 1).getName();
        }

        /**
         * @param departureDate departure date.
         * @param locale current locale.
         * @return day of week and date when train departs from the departure station as string.
         */
        public String getDepartureDayOfWeekAndDateAsString(String departureDate, String locale) {
            LocalDate date = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String dayOfWeek;
            if (locale != null && locale.equals("en")) {
                dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            } else {
                dayOfWeek = DayOfWeekLocaleUA.of(date.getDayOfWeek().getValue());
            }
            return dayOfWeek +
                    ", " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH));
        }

        /**
         * @param departureDate departure date.
         * @param fromStationId departure station id.
         * @return train departure date and time.
         */
        public LocalDateTime getDepartureDateTime(int fromStationId, String departureDate) {
            String dateTime = departureDate + " " + getArrivalTime(fromStationId);
            return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        }

        /**
         * @param fromStationId departure station id.
         * @param toStationId destination station id.
         * @param departureDate departure date.
         * @return day of week and date when train arrive to destination station as string.
         */
        public String getDestinationDayOfWeekAndDateAsString(int fromStationId, int toStationId, String departureDate, String locale) {
            LocalDate date = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            int daysOfTrip = (Integer.parseInt(timeSinceStartMap.get(toStationId).split(":")[0]) - Integer.parseInt(timeSinceStartMap.get(fromStationId).split(":")[0])) / 24;
            LocalDate destinationDate;
            String dayOfWeek;
            if (LocalTime.parse(getArrivalTime(fromStationId)).compareTo(LocalTime.parse(getArrivalTime(toStationId))) > 0) {
                destinationDate = date.plusDays(1 + daysOfTrip);
            } else {
                destinationDate = date.plusDays(daysOfTrip);
            }
            if (locale != null && locale.equals("en")) {
                dayOfWeek = destinationDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            } else {
                dayOfWeek = DayOfWeekLocaleUA.of(destinationDate.getDayOfWeek().getValue());
            }
            return dayOfWeek +
                    ", " + destinationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH));
        }

        /**
         * @param fromStationId departure station id.
         * @param toStationId destination station id.
         * @param departureDate departure date.
         * @return train destination date and time.
         */
        public LocalDateTime getDestinationDateTime(int fromStationId, int toStationId, String departureDate) {
            return getDepartureDateTime(fromStationId, departureDate)
                    .plusHours(Integer.parseInt(timeSinceStartMap.get(toStationId).split(":")[0]) - Integer.parseInt(timeSinceStartMap.get(fromStationId).split(":")[0]))
                    .plusMinutes(Integer.parseInt(timeSinceStartMap.get(toStationId).split(":")[1]));
        }

        /**
         * @param stationId - id of destination station.
         * @return time duration between first station and destination station as string.
         */
        public String getArrivalTime(int stationId) {
            return departureTime.plusHours(Long.parseLong(timeSinceStartMap.get(stationId).split(":")[0]))
                    .plusMinutes(Long.parseLong(timeSinceStartMap.get(stationId).split(":")[1])).toString();
        }

        /**
         * @param fromStationId - id of departure station.
         * @param toStationId - id of destination station.
         * @return trip duration as string.
         */
        public String getDurationTrip(int fromStationId, int toStationId, String locale) {
            LocalDateTime startPoint = LocalDateTime.MIN;
            LocalDateTime duration = startPoint.plusHours(Long.parseLong(timeSinceStartMap.get(toStationId).split(":")[0]))
                    .plusMinutes(Long.parseLong(timeSinceStartMap.get(toStationId).split(":")[1]))
                    .minusHours(Long.parseLong(timeSinceStartMap.get(fromStationId).split(":")[0]))
                    .minusMinutes(Long.parseLong(timeSinceStartMap.get(fromStationId).split(":")[1]));
            String result;
            if (duration.getDayOfMonth() != startPoint.getDayOfMonth()) {
                if ("en".equals(locale)) {
                    result = duration.getDayOfMonth() - startPoint.getDayOfMonth() + " d. " + duration.toLocalTime();
                } else {
                    result = duration.getDayOfMonth() - startPoint.getDayOfMonth() + " д. " + duration.toLocalTime();
                }
            } else {
                result = duration.toLocalTime().toString();
            }
            return result;
        }

        /**
         * @param fromStationId id of departure station.
         * @param toStationId id of destination station.
         * @param carriageType {@link Carriage} type.
         * @return cost of trip between departure station and destination station.
         */
        public String getCostOfTripAsString(int fromStationId, int toStationId, String carriageType) {
            int tripDistance = distanceFromStartMap.get(toStationId) - distanceFromStartMap.get(fromStationId);
            double costOfTrip = (Util.getBasicTicketCost()
                    + tripDistance * Util.getOneKilometerRoadCost()
                    * (1 - getCoefficientDependingOnDistanceOfTrip(tripDistance)))
                    * Util.getCoefficientByCarriageType(carriageType);
            return new DecimalFormat("#0.00").format(costOfTrip);
        }

        private double getCoefficientDependingOnDistanceOfTrip(int tripDistance) {
            double coefficient = BigDecimal.valueOf((double) tripDistance / 1000).setScale(1, RoundingMode.FLOOR).doubleValue();
            if (coefficient > 0.5) {
                coefficient = 0.5;
            }
            return coefficient;
        }

        /**
         * Check that station is on the route.
         * @param stationId {@link Station} id.
         * @return true if {@link Station} exists on the route or false if not.
         */
        public boolean checkIfStationIsOnTheRoute(int stationId) {
            return stations.stream().anyMatch(station -> station.getId() == stationId);
        }

        /**
         * Check that new time since start of the {@link Station} on the route is between times since start of the nearest {@link Station}.
         * @param stationId {@link Station} id.
         * @param timeSinceStart new time since start of the {@link Station}.
         * @return true if time since start is between or false if not.
         */
        public boolean checkIfNewTimeSinceStartCorrect(int stationId, String timeSinceStart) {
            if (stations.size() <= 1) {
                return true;
            }
            LocalDateTime newTimeSinceStart = LocalDateTime.MIN.plusHours(Integer.parseInt(timeSinceStart.split(":")[0]))
                    .plusMinutes(Integer.parseInt(timeSinceStart.split(":")[1]));
            String timeSinceStartPrevious;
            String timeSinceStartNext;
            LocalDateTime timeSinceStartPreviousStation;
            LocalDateTime timeSinceStartNextStation;
            if (stations.get(0).getId() == stationId) {
                timeSinceStartNext = timeSinceStartMap.get(stations.get(1).getId());
                timeSinceStartNextStation = LocalDateTime.MIN.plusHours(Integer.parseInt(timeSinceStartNext.split(":")[0]))
                        .plusMinutes(Integer.parseInt(timeSinceStartNext.split(":")[1]));
                return timeSinceStartNextStation.compareTo(newTimeSinceStart) > 0;
            }
            if (stations.get(stations.size() - 1).getId() == stationId) {
                timeSinceStartPrevious = timeSinceStartMap.get(stations.get(stations.size() - 2).getId());
                timeSinceStartPreviousStation = LocalDateTime.MIN.plusHours(Integer.parseInt(timeSinceStartPrevious.split(":")[0]))
                        .plusMinutes(Integer.parseInt(timeSinceStartPrevious.split(":")[1]));
                return newTimeSinceStart.compareTo(timeSinceStartPreviousStation) > 0;
            }
            int index = 0;
            for (int i = 0; i < stations.size(); i++) {
                if (stations.get(i).getId() == stationId) {
                    index = i;
                }
            }
            timeSinceStartPrevious = timeSinceStartMap.get(stations.get(index - 1).getId());
            timeSinceStartNext = timeSinceStartMap.get(stations.get(index + 1).getId());
            timeSinceStartPreviousStation = LocalDateTime.MIN.plusHours(Integer.parseInt(timeSinceStartPrevious.split(":")[0]))
                    .plusMinutes(Integer.parseInt(timeSinceStartPrevious.split(":")[1]));
            timeSinceStartNextStation = LocalDateTime.MIN.plusHours(Integer.parseInt(timeSinceStartNext.split(":")[0]))
                    .plusMinutes(Integer.parseInt(timeSinceStartNext.split(":")[1]));
            return newTimeSinceStart.compareTo(timeSinceStartPreviousStation) > 0 && timeSinceStartNextStation.compareTo(newTimeSinceStart) > 0;
        }

        /**
         * Check that new distance from start of the {@link Station} on the route is between distances from start of the nearest {@link Station}.
         * @param stationId {@link Station} id.
         * @param distanceFromStart new distance from start of the {@link Station}.
         * @return true if distance from start is between or false if not.
         */
        public boolean checkIfNewDistanceFromStartCorrect(int stationId, int distanceFromStart) {
            if (stations.size() <= 1) {
                return true;
            }
            int distanceFromStartPrevious;
            int distanceFromStartNext;
            if (stations.get(0).getId() == stationId) {
                distanceFromStartNext = distanceFromStartMap.get(stations.get(1).getId());
                return distanceFromStartNext > distanceFromStart;
            }
            if (stations.get(stations.size() - 1).getId() == stationId) {
                distanceFromStartPrevious = distanceFromStartMap.get(stations.get(stations.size() - 2).getId());
                return distanceFromStart > distanceFromStartPrevious;
            }
            int index = 0;
            for (int i = 0; i < stations.size(); i++) {
                if (stations.get(i).getId() == stationId) {
                    index = i;
                }
            }
            distanceFromStartPrevious = distanceFromStartMap.get(stations.get(index - 1).getId());
            distanceFromStartNext = distanceFromStartMap.get(stations.get(index + 1).getId());
            return distanceFromStart > distanceFromStartPrevious && distanceFromStartNext > distanceFromStart;
        }

        public int getDistanceFromFirstStation(int stationId) {
            return distanceFromStartMap.getOrDefault(stationId, 0);
        }

        public LocalTime getTimeStop(int stationId) {
            return stopTimeMap.get(stationId);
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

        public String getTimeSinceStart(Integer stationId) {
            return timeSinceStartMap.computeIfPresent(stationId, (id, time) -> time.substring(0, 5));
        }

        public LocalTime getStopTime(Integer stationId) {
            return stopTimeMap.get(stationId);
        }

        public void addStation(Station station) {
            stations.add(station);
        }

        public void addStationByTimeSinceStart(Station station, String timeSinceStart) {
            LocalDateTime newTimeSinceStart = LocalDateTime.MIN.plusHours(Integer.parseInt(timeSinceStart.split(":")[0]))
                    .plusMinutes(Integer.parseInt(timeSinceStart.split(":")[1]));
            int index = 0;
            for (int i = 0; i < stations.size(); i++) {
                String stringTime = timeSinceStartMap.get(stations.get(i).getId());
                LocalDateTime time = LocalDateTime.MIN.plusHours(Integer.parseInt(stringTime.split(":")[0]))
                        .plusMinutes(Integer.parseInt(stringTime.split(":")[1]));
                if (newTimeSinceStart.compareTo(time) > 0) {
                    index = i + 1;
                    break;
                }
            }
            stations.add(index, station);
        }

        public void addTimeSinceStart(Integer stationId, String timeSinceStart) {
            timeSinceStartMap.put(stationId, timeSinceStart);
        }

        public void addStopTime(Integer stationId, LocalTime stopTime) {
            stopTimeMap.put(stationId, stopTime);
        }

        public void addDistanceFromStart(Integer stationId, Integer distanceFromStart) {
            distanceFromStartMap.put(stationId, distanceFromStart);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Route route = (Route) o;
            return Objects.equals(stations, route.stations) && Objects.equals(timeSinceStartMap, route.timeSinceStartMap) && Objects.equals(stopTimeMap, route.stopTimeMap) && Objects.equals(distanceFromStartMap, route.distanceFromStartMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(stations, timeSinceStartMap, stopTimeMap, distanceFromStartMap);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Train train = (Train) o;
        return id == train.id && Objects.equals(route, train.route) && Objects.equals(number, train.number) && Objects.equals(departureTime, train.departureTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, id, number, departureTime);
    }
}
