package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLTrainDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLTrainDAOQuery {

    public static final String GET_ALL_TRAINS = "SELECT id, number, departure_time FROM railwayticketofficedb.trains";
    public static final String GET_CARRIAGES_FOR_TRAINS = "SELECT carriages.id, number, type, max_seats, train_id " +
            "FROM railwayticketofficedb.types, railwayticketofficedb.carriages, railwayticketofficedb.trains_carriages " +
            "WHERE type_id = types.id AND carriages.id = trains_carriages.carriage_id AND trains_carriages.train_id IN ";
    public static final String GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE = "SELECT id, number, departure_time " +
            "FROM railwayticketofficedb.trains, railwayticketofficedb.trains_stations, railwayticketofficedb.schedules " +
            "WHERE trains.id = trains_stations.train_id AND trains_stations.station_id IN (?, ?) AND trains.id = schedules.train_id AND schedules.day = ? GROUP BY trains.id";
    public static final String GET_ROUTES_FOR_TRAINS = "SELECT time_since_start, stop_time, distance_from_start, train_id, station_id, ";
    public static final String GET_FREE_SEATS_FOR_TRAINS_BY_DATE = "SELECT train_id, carriage_id, seat_number " +
            "FROM railwayticketofficedb.schedules WHERE day = ? AND train_id in ";
    public static final String GET_STATION_NAME = "(SELECT name FROM stations WHERE trains_stations.station_id = stations.id) AS name " +
            "FROM trains_stations WHERE trains_stations.train_id IN ";
    public static final String GET_STATION_EN_NAME = "(SELECT name FROM stations_en WHERE trains_stations.station_id = stations_en.station_id) AS name " +
            "FROM trains_stations WHERE trains_stations.train_id IN ";
    public static final String ORDER_BY = "ORDER BY distance_from_start";
    public static final String CHECK_IF_TRAIN_EXISTS = "SELECT id FROM railwayticketofficedb.trains WHERE number = ?";
    public static final String ADD_TRAIN = "INSERT INTO railwayticketofficedb.trains VALUES (default, ?, ?)";
    public static final String DELETE_TRAIN = "DELETE FROM railwayticketofficedb.trains WHERE id = ?";
    public static final String EDIT_TRAIN = "UPDATE railwayticketofficedb.trains SET number = ?, departure_time = ? WHERE id = ?";
    public static final String GET_CARRIAGES_TYPES = "SELECT id, type FROM railwayticketofficedb.types";
    public static final String CHECK_IF_TRAIN_HAS_CARRIAGE_WITH_THIS_NUMBER = "SELECT id " +
            "FROM railwayticketofficedb.trains_carriages, railwayticketofficedb.carriages " +
            "WHERE train_id = ? AND carriage_id = carriages.id AND number = ?";
    public static final String CHECK_IF_CARRIAGE_EXISTS = "SELECT id FROM railwayticketofficedb.carriages WHERE number = ? AND type_id = ?";
    public static final String CREATE_CARRIAGE = "INSERT INTO railwayticketofficedb.carriages VALUES (default, ?, ?)";
    public static final String ADD_CARRIAGE_TO_TRAIN = "INSERT INTO railwayticketofficedb.trains_carriages VALUES (?, ?)";
    public static final String GET_CARRIAGE_MAX_SEATS = "SELECT max_seats FROM railwayticketofficedb.types, railwayticketofficedb.carriages " +
            "WHERE types.id = carriages.type_id AND carriages.id = ?";
    public static final String DELETE_CARRIAGE_FROM_TRAIN = "DELETE FROM railwayticketofficedb.trains_carriages WHERE train_id = ? AND carriage_id = ?";
    public static final String CHECK_IF_CARRIAGE_TYPE_EXISTS = "SELECT id FROM railwayticketofficedb.types WHERE type = ?";
    public static final String ADD_CARRIAGE_TYPE = "INSERT INTO railwayticketofficedb.types VALUES (default, ?, ?)";
    public static final String DELETE_CARRIAGE_TYPE = "DELETE FROM railwayticketofficedb.types WHERE id = ?";
    public static final String EDIT_CARRIAGE_TYPE = "UPDATE railwayticketofficedb.types SET type = ? WHERE id = ?";
    public static final String DELETE_STATION_FROM_TRAIN_ROUTE = "DELETE FROM trains_stations WHERE train_id = ? AND station_id = ?";
    public static final String ADD_STATION_TO_TRAIN_ROUTE = "INSERT INTO trains_stations VALUES (?, ?, ?, ?, ?)";
    public static final String EDIT_STATION_DATA_ON_TRAIN_ROUTE = "UPDATE trains_stations SET time_since_start = ?, stop_time = ?, distance_from_start = ? WHERE train_id = ? AND station_id = ?";
    public static final String GET_TRAIN = "SELECT number, departure_time FROM railwayticketofficedb.trains WHERE id = ?";
}
