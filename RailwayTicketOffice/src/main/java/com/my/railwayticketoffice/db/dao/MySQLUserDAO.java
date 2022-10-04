package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements {@link UserDAO} interface methods for MySQL.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLUserDAO implements UserDAO {

    @Override
    public User getUser(Connection connection, String userEmail) throws SQLException {
        User user = new User();
        PreparedStatement pstm = connection.prepareStatement(MySQLUserDAOQuery.GET_USER);
        pstm.setString(1, userEmail);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setRole(rs.getString("role"));
            user.setRegistered(rs.getBoolean("registered"));
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
    public void updateUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.UPDATE_USER);
        pstmt.setString(1, user.getPassword());
        pstmt.setString(2, user.getFirstName());
        pstmt.setString(3, user.getLastName());
        pstmt.setBoolean(4, user.isRegistered());
        pstmt.setInt(5, user.getId());
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit user data in database");
        }
    }

    @Override
    public List<User> getUsersThatPurchasedSeatOnTrain(Connection connection, int trainId) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.GET_USERS_THAT_PURCHASED_SEAT_ON_TRAIN);
        pstmt.setInt(1, trainId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getUsersThatPurchasedSeatOnTrainCarriage(Connection connection, int trainId, int carriageId) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.GET_USERS_THAT_PURCHASED_SEAT_IN_CARRIAGE_ON_TRAIN);
        pstmt.setInt(1, trainId);
        pstmt.setInt(2, carriageId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getUsersThatPurchasedSeatOnTrainAtDate(Connection connection, String departureDate, int trainId) throws SQLException {
        List<User> users = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(MySQLUserDAOQuery.GET_USERS_THAT_PURCHASED_SEAT_ON_TRAIN_AT_DATE);
        pstmt.setString(1, departureDate);
        pstmt.setInt(2, trainId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            users.add(user);
        }
        return users;
    }
}
