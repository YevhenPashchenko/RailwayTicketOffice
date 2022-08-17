package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLTrainDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLTrainDAOQuery {

    public static final String GET_ALL_TRAINS_DATA_FOR_SCHEDULE = "SELECT id, seats FROM trains";
    public static final String GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE = "SELECT id, number, departure_time, (SELECT available_seats) AS available_seats " +
            "FROM trains, trains_stations, schedules " +
            "WHERE trains.id = trains_stations.train_id AND trains_stations.station_id IN (?, ?) AND trains.id = schedules.train_id AND schedules.day = ? AND schedules.available_seats > 0 GROUP BY trains.number HAVING COUNT(trains.number) = 2";
    public static final String GET_ROUTES_FOR_TRAINS = "SELECT time_since_start, stop_time, distance_from_start, train_id, station_id, " +
            "(SELECT name FROM stations WHERE trains_stations.station_id = stations.id) AS name " +
            "FROM trains_stations WHERE trains_stations.train_id IN ";
    public static final String ORDER_BY = "ORDER BY distance_from_start";
    public static final String GET_TRAIN = "SELECT DISTINCT number, departure_time, (SELECT available_seats) AS available_seats " +
            "FROM trains, schedules WHERE trains.id = schedules.train_id AND trains.id = ?";
    public static final String GET_ROUTE_FOR_TRAIN = "SELECT time_since_start, stop_time, distance_from_start, train_id, station_id, " +
            "(SELECT name FROM stations WHERE trains_stations.station_id = stations.id) AS name " +
            "FROM trains_stations WHERE trains_stations.train_id = ? ORDER BY distance_from_start";
}
