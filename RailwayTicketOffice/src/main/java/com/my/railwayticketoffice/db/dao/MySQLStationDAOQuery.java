package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLStationDAO} class.
 */
public class MySQLStationDAOQuery {

    public static final String GET_STATIONS = "SELECT id, name FROM stations";
}
