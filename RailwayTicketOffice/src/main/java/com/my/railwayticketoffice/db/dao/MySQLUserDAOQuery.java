package com.my.railwayticketoffice.db.dao;

/**
 * Utility class that contains MySQL queries for {@link MySQLUserDAO} class.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLUserDAOQuery {

    public static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?";
}
