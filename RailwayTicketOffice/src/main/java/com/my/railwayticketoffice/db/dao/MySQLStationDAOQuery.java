package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLStationDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLStationDAOQuery {

    public static final String GET_STATIONS = "SELECT id, name FROM stations";
    public static final String ADD_STATION = "INSERT INTO stations VALUES (default, ?)";
    public static final String DELETE_STATION = "DELETE FROM stations WHERE id = ?";
}
