package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing user data.
 *
 * @author Yevhen Pashchenko
 */
public interface UserDAO {

    /**
     * When a class implementing interface {@link UserDAO} call this method should be return a user data
     * by user E-mail.
     * @param connection Connection object.
     * @param userEmail user E-mail.
     * @return {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    User getUser(Connection connection, String userEmail) throws SQLException;

    /**
     * When a class implementing interface {@link UserDAO} call this method should be added user to database
     * @param connection Connection object.
     * @param user new {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    void addUser(Connection connection, User user) throws SQLException;

    /**
     * When a class implementing interface {@link UserDAO} call this method should be edit user data in database
     * @param connection Connection object.
     * @param user changed {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    void updateUser(Connection connection, User user) throws SQLException;

    /**
     * When a class implementing interface {@link UserDAO} call this method should be return users that purchased seat
     * on this {@link com.my.railwayticketoffice.entity.Train}.
     * @param connection Connection object.
     * @param trainId {@link com.my.railwayticketoffice.entity.Train} id.
     * @return list of {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    List<User> getUsersThatPurchasedSeatOnTrain(Connection connection, int trainId) throws SQLException;

    /**
     * When a class implementing interface {@link UserDAO} call this method should be return users that purchased seat
     * in {@link com.my.railwayticketoffice.entity.Train.Carriage} on this {@link com.my.railwayticketoffice.entity.Train}.
     * @param connection Connection object.
     * @param trainId {@link com.my.railwayticketoffice.entity.Train} id.
     * @param carriageId {@link com.my.railwayticketoffice.entity.Train.Carriage} id.
     * @return list of {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    List<User> getUsersThatPurchasedSeatOnTrainCarriage(Connection connection, int trainId, int carriageId) throws SQLException;

    /**
     * When a class implementing interface {@link UserDAO} call this method should be return users that purchased seat
     * on this {@link com.my.railwayticketoffice.entity.Train} at this date.
     * @param connection Connection object.
     * @param departureDate departure date.
     * @param trainId {@link com.my.railwayticketoffice.entity.Train} id.
     * @return list of {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    List<User> getUsersThatPurchasedSeatOnTrainAtDate(Connection connection, String departureDate, int trainId) throws SQLException;
}
