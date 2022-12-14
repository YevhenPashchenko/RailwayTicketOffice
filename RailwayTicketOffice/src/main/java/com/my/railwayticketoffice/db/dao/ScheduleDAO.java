package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * Interface for managing train schedule data.
 *
 * @author Yevhen Pashchenko
 */
public interface ScheduleDAO {

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should clear all schedule data.
     * @param connection Connection object.
     * @throws SQLException if a database access error occurs.
     */
    void clearTable(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should add all train data to schedule.
     * @param connection Connection object.
     * @param scheduleDates list string of dates. String date must be match with pattern "yyyy-MM-dd".
     * @param trains list of {@link Train} data that needed for train schedule data.
     * @param user {@link User}.
     * @throws SQLException if a database access error occurs.
     */
    void addData(Connection connection, List<String> scheduleDates, List<Train> trains, User user) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should delete train data from schedule
     * which date less than current date.
     * @param connection Connection object.
     * @param currentDate string date. String date must be match with pattern "yyyy-MM-dd".
     * @throws SQLException if a database access error occurs.
     */
    void deleteData(Connection connection, String currentDate) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be return a number of train
     * available seats on selected date.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @param selectedDate - selected date.
     * @return - number of train available seats.
     * @throws SQLException ??? if a database access error occurs.
     */
    int getTrainAvailableSeatsOnThisDate(Connection connection, int trainId, String selectedDate) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be checked if record exists in the database.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @return - true if record exists or false if not exists.
     * @throws SQLException ??? if a database access error occurs.
     */
    boolean checkIfRecordExists(Connection connection, int trainId) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be deleted train from schedule at this departure date.
     * @param connection Connection object.
     * @param departureDate departure date.
     * @param trainId train id.
     * @throws SQLException if a database access error occurs.
     */
    void deleteTrainFromScheduleAtDate(Connection connection, String departureDate, int trainId) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be changed a number of train
     * available seats on selected date.
     * @param connection Connection object.
     * @param userId {@link User} id.
     * @param trainId {@link Train} id.
     * @param selectedDate selected date.
     * @param carriageId {@link Train.Carriage} id.
     * @param seatsNumbers list of seats from {@link Train.Carriage} that must be deleted from schedule.
     * @throws SQLException if a database access error occurs.
     */
    void changeTrainAvailableSeatsOnThisDate(Connection connection, int userId, int trainId, String selectedDate, int carriageId, List<Integer> seatsNumbers) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be changed {@link com.my.railwayticketoffice.entity.Train.Carriage} id
     * where is this {@link Train} id.
     * @param connection Connection object.
     * @param newCarriageId new {@link Train.Carriage} id.
     * @param trainId {@link Train} id.
     * @param carriageId {@link Train.Carriage} id.
     * @throws SQLException if a database access error occurs.
     */
    void editCarriageData(Connection connection, int newCarriageId, int trainId, int carriageId) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be checked that {@link User}
     * id is not null.
     * @param connection Connection object.
     * @param departureDate departure date.
     * @param trainId {@link Train} id.
     * @param carriageId {@link Train.Carriage} id.
     * @param seatNumber seat number.
     * @return {@link User} id if this seat booked or null if not.
     * @throws SQLException if a database access error occurs.
     */
    Integer checkIfSeatBooked(Connection connection, String departureDate, int trainId, int carriageId, int seatNumber) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should set the value of {@link User} id equals null.
     * @param connection Connection object.
     * @param departureDate departure date.
     * @param trainId {@link Train} id.
     * @param carriageId {@link Train.Carriage} id.
     * @param seatNumber seat number.
     * @throws SQLException if a database access error occurs.
     */
    void returnTicket(Connection connection, String departureDate, int trainId, int carriageId, int seatNumber) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should return true if any {@link Train.Carriage} id
     * with this type id exists in schedule or false if not.
     * @param connection Connection object.
     * @param typeId {@link Train.Carriage} type id.
     * @return true if any {@link Train.Carriage} id with this type id exists in schedule or false if not.
     * @throws SQLException if a database access error occurs.
     */
    boolean checkIfCarriagesIsInSchedule(Connection connection, int typeId) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should return true if any {@link Train} has
     * this {@link com.my.railwayticketoffice.entity.Station} on its route and is in the schedule.
     * @param connection Connection object.
     * @param stationId {@link com.my.railwayticketoffice.entity.Station} id.
     * @return true if any {@link Train} has this {@link com.my.railwayticketoffice.entity.Station} on its route and is in the schedule or false if not.
     * @throws SQLException if a database access error occurs.
     */
    boolean checkIfTrainsThatHasThisStationOnTheRouteIsInSchedule(Connection connection, int stationId) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should return true if any seat in this {@link Train}
     * booked at this departure date.
     * @param connection Connection object.
     * @param departureDate departure date.
     * @param trainId {@link Train} id.
     * @return true if any seat in this {@link Train} booked at this departure date or false if not.
     * @throws SQLException if a database access error occurs.
     */
    boolean checkIfTrainHasBookedSeatsAtDate(Connection connection, String departureDate, int trainId) throws SQLException;
}
