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
}
