package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLScheduleDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLScheduleDAOQuery {

    public static final String CLEAR_TABLE = "TRUNCATE schedules";
    public static final String ADD_DATA = "INSERT INTO schedules VALUES ";
    public static final String VALUES_FOR_ADD_DATA = "(?, ?, ?)";
    public static final String DELETE_DATA = "DELETE FROM schedules WHERE day < ?";
}
