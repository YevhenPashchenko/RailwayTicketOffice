package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLScheduleDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLScheduleDAOQuery {

    public static final String CLEAR_TABLE = "TRUNCATE railwayticketofficedb.schedules";
    public static final String ADD_DATA = "INSERT INTO railwayticketofficedb.schedules VALUES ";
    public static final String VALUES_FOR_ADD_DATA = "(?, ?, ?, ?, ?)";
    public static final String DELETE_DATA = "DELETE FROM railwayticketofficedb.schedules WHERE day < ?";
    public static final String GET_AVAILABLE_SEATS = "SELECT available_seats FROM railwayticketofficedb.schedules WHERE train_id = ? AND day = ?";
    public static final String CHECK_IF_RECORD_EXISTS = "SELECT EXISTS(SELECT train_id FROM railwayticketofficedb.schedules WHERE train_id = ?) AS exist";
    public static final String DELETE_TRAIN_FROM_SCHEDULE = "DELETE FROM railwayticketofficedb.schedules WHERE day = ? AND train_id = ?";
    public static final String CHANGE_AVAILABLE_SEATS = "UPDATE railwayticketofficedb.schedules SET user_id = ? WHERE day = ? AND train_id = ? AND carriage_id = ? AND seat_number in ";
    public static final String EDIT_CARRIAGE_DATA = "UPDATE railwayticketofficedb.schedules SET carriage_id = ? WHERE train_id = ? AND carriage_id = ?";
    public static final String CHECK_IF_SEAT_BOOKED = "SELECT user_id FROM railwayticketofficedb.schedules WHERE day = ? AND train_id = ? AND carriage_id = ? AND seat_number = ?";
    public static final String RETURN_TICKET = "UPDATE railwayticketofficedb.schedules SET user_id = NULL WHERE day = ? AND train_id = ? AND carriage_id = ? AND seat_number = ?";
    public static final String CHECK_IF_CARRIAGES_IS_IN_SCHEDULE = "SELECT count(DISTINCT carriage_id) AS count FROM railwayticketofficedb.schedules, railwayticketofficedb.carriages " +
            "WHERE schedules.carriage_id = carriages.id AND type_id = ?";
    public static final String CHECK_IF_TRAINS_THAT_HAS_THIS_STATION_ON_THE_ROUTE_IS_IN_SCHEDULE = "SELECT count(DISTINCT schedules.train_id) AS count " +
            "FROM railwayticketofficedb.schedules, railwayticketofficedb.trains, railwayticketofficedb.trains_stations " +
            "WHERE schedules.train_id = trains.id AND trains.id = trains_stations.train_id AND station_id = ?";
    public static final String CHECK_IF_TRAIN_HAS_BOOKED_SEATS_AT_DATE = "SELECT count(seat_number) AS count " +
            "FROM railwayticketofficedb.schedules WHERE day = ? AND train_id = ? AND user_id IS NOT NULL";
}
