package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLScheduleDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLScheduleDAOQuery {

    public static final String CLEAR_TABLE = "TRUNCATE railwayticketofficedb.schedules";
    public static final String ADD_DATA = "INSERT INTO railwayticketofficedb.schedules VALUES ";
    public static final String VALUES_FOR_ADD_DATA = "(?, ?, ?, ?)";
    public static final String DELETE_DATA = "DELETE FROM railwayticketofficedb.schedules WHERE day < ?";
    public static final String DELETE_CARRIAGE_FROM_SCHEDULE = "DELETE FROM railwayticketofficedb.schedules WHERE train_id= ? AND carriage_id = ?";
    public static final String GET_AVAILABLE_SEATS = "SELECT available_seats FROM schedules WHERE train_id = ? AND day = ?";
    public static final String CHECK_IF_RECORD_EXISTS = "SELECT EXISTS(SELECT train_id FROM schedules WHERE train_id = ?) AS exist";
    public static final String DELETE_TRAIN_FROM_SCHEDULE = "DELETE FROM schedules WHERE train_id = ?";
    public static final String CHANGE_AVAILABLE_SEATS = "DELETE FROM railwayticketofficedb.schedules WHERE day = ? and train_id = ? and carriage_id = ? and seat_number in ";
}
