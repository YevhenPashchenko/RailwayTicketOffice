package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLUserDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLUserDAOQuery {

    public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
    public static final String ADD_USER = "INSERT INTO users VALUES(default, ?, ?, ?, ?, 'user')";
    public static final String UPDATE_USER_WITH_PASSWORD = "UPDATE users SET password = ?, first_name = ?, last_name = ? where id = ?";
    public static final String UPDATE_USER = "UPDATE users SET first_name = ?, last_name = ? where id = ?";
}
