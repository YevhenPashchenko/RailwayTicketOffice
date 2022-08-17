package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for managing user data.
 *
 * @author Yevhen Pashchenko
 */
public interface UserDAO {

    /**
     * When a class implementing interface {@link UserDAO} call this method should be return a user data
     * by user E-mail.
     * @param connection - Connection object.
     * @param userEmail - user E-mail.
     * @return {@link User}.
     * @throws SQLException – if a database access error occurs.
     */
    User getUser(Connection connection, String userEmail) throws SQLException;
}
