package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLTrainDAO} class.
 */
public class MySQLTrainDAOQuery {

    public static final String GET_ALL_TRAINS_DATA_FOR_SCHEDULE = "SELECT id, seats FROM trains";
    public static final String GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE = "";
}
