package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLStationDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLStationDAOQuery {

    public static final String GET_STATIONS = "SELECT id, name FROM stations";
    public static final String GET_EN_STATIONS = "SELECT station_id as id, name FROM stations_en";
    public static final String ADD_STATION = "INSERT INTO stations VALUES (default, ?)";
    public static final String ADD_STATION_EN = "INSERT INTO stations_en VALUES (?, ?)";
    public static final String DELETE_STATION = "DELETE FROM stations WHERE id = ?";
    public static final String EDIT_STATION = "UPDATE stations SET name = ? WHERE id = ?";
    public static final String EDIT_EN_STATION = "UPDATE stations_en SET name = ? WHERE station_id = ?";
}
