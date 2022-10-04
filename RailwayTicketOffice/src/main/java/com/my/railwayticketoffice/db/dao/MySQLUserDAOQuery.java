package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLUserDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLUserDAOQuery {

    public static final String GET_USER = "SELECT * FROM users WHERE email = ?";
    public static final String ADD_USER = "INSERT INTO users VALUES(default, ?, ?, ?, ?, 'user', false)";
    public static final String UPDATE_USER = "UPDATE users SET password = ?, first_name = ?, last_name = ?, registered = ? where id = ?";
    public static final String GET_USERS_THAT_PURCHASED_SEAT_ON_TRAIN = "SELECT DISTINCT email, first_name, last_name " +
            "FROM railwayticketofficedb.schedules, railwayticketofficedb.users WHERE train_id = ? AND user_id IS NOT NULL AND user_id = users.id";
    public static final String GET_USERS_THAT_PURCHASED_SEAT_IN_CARRIAGE_ON_TRAIN = "SELECT DISTINCT email, first_name, last_name " +
            "FROM railwayticketofficedb.schedules, railwayticketofficedb.users WHERE train_id = ? AND carriage_id = ? AND user_id IS NOT NULL AND user_id = users.id";
    public static final String GET_USERS_THAT_PURCHASED_SEAT_ON_TRAIN_AT_DATE = "SELECT DISTINCT email, first_name, last_name " +
            "FROM railwayticketofficedb.schedules, railwayticketofficedb.users WHERE day = ? AND train_id = ? AND user_id IS NOT NULL AND user_id = users.id";
}
