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

    @Override
    public void addUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.ADD_USER);
        pstmt.setString(1, user.getEmail());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getFirstName());
        pstmt.setString(4, user.getLastName());
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to register new user in database");
        }
    }

    @Override
    public void updateUserWithPassword(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.UPDATE_USER_WITH_PASSWORD);
        pstmt.setString(1, user.getPassword());
        pstmt.setString(2, user.getFirstName());
        pstmt.setString(3, user.getLastName());
        pstmt.setInt(4, user.getId());
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit user data in database");
        }
    }

    @Override
    public void updateUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.UPDATE_USER);
        pstmt.setString(1, user.getFirstName());
        pstmt.setString(2, user.getLastName());
        pstmt.setInt(3, user.getId());
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit user data in database");
        }
    }
}
