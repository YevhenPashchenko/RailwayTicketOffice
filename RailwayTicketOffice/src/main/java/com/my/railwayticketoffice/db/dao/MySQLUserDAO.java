package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that implements {@link UserDAO} interface methods for MySQL.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLUserDAO implements UserDAO {

    @Override
    public User getUser(Connection connection, String userEmail) throws SQLException {
        User user = new User();
        PreparedStatement pstm = connection.prepareStatement(MySQLUserDAOQuery.GET_USER_BY_EMAIL);
        pstm.setString(1, userEmail);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setRole(rs.getString("role"));
        }
        return user;
    }
}
