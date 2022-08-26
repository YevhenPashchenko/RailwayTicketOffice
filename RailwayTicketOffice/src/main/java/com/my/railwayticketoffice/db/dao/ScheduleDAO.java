package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

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
     * When a class implementing interface {@link ScheduleDAO} call this method should be clear a train schedule data.
     * @param connection - Connection object.
     * @throws SQLException – if a database access error occurs.
     */
    void clearTable(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be added train schedule data.
     * @param connection - Connection object.
     * @param scheduleDates - list string of dates. String date must be match with pattern "yyyy-MM-dd".
     * @param trains - list of {@link Train} data that needed for train schedule data.
     * @throws SQLException – if a database access error occurs.
     */
    void addData(Connection connection, List<String> scheduleDates, List<Train> trains) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be deleted train schedule data
     * which date less than current date.
     * @param connection - Connection object.
     * @param currentDate - string date. String date must be match with pattern "yyyy-MM-dd".
     * @throws SQLException – if a database access error occurs.
     */
    void deleteData(Connection connection, String currentDate) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be return a number of train
     * available seats on selected date.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @param selectedDate - selected date.
     * @return - number of train available seats.
     * @throws SQLException – if a database access error occurs.
     */
    int getTrainAvailableSeatsOnThisDate(Connection connection, int trainId, String selectedDate) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be changed a number of train
     * available seats on selected date.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @param selectedDate - selected date.
     * @param availableSeatsNow - new number of train available seats.
     * @throws SQLException – if a database access error occurs.
     */
    void changeTrainAvailableSeatsOnThisDate(Connection connection, int trainId, String selectedDate, int availableSeatsNow) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be checked if record exists in the database.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @return - true if record exists or false if not exists.
     * @throws SQLException – if a database access error occurs.
     */
    boolean checkIfRecordExists(Connection connection, int trainId) throws SQLException;

    /**
     * When a class implementing interface {@link ScheduleDAO} call this method should be deleted train from schedule.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @throws SQLException – if a database access error occurs.
     */
    void deleteTrainFromSchedule(Connection connection, int trainId) throws SQLException;
}
