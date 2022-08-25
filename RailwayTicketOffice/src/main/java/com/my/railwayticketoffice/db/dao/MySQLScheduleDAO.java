package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

import java.sql.*;
import java.util.List;

/**
 * Class that implements {@link ScheduleDAO} interface methods for MySQL.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLScheduleDAO implements ScheduleDAO {

    @Override
    public void clearTable(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(MySQLScheduleDAOQuery.CLEAR_TABLE);
    }

    @Override
    public void addData(Connection connection, List<String> scheduleDates, List<Train> trains) throws SQLException {
        int count = 1;
        StringBuilder query = new StringBuilder();
        query.append(MySQLScheduleDAOQuery.ADD_DATA)
                .append(MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA);
        for (int j = 1; j < scheduleDates.size() * trains.size(); j++) {
            query.append(", ")
                    .append(MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA);
        }
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (String scheduleDate:
             scheduleDates) {
            for (Train train:
                 trains) {
                pstmt.setString(count++, scheduleDate);
                pstmt.setInt(count++, train.getSeats());
                pstmt.setInt(count++, train.getId());
            }
        }
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Data not added to train schedule");
        }
    }

    @Override
    public void deleteData(Connection connection, String currentDate) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_DATA);
        pstmt.setString(1, currentDate);
        int deletedRows = pstmt.executeUpdate();
        if (deletedRows == 0) {
            throw new SQLException("Data not deleted from train schedule");
        }
    }

    @Override
    public int getTrainAvailableSeatsOnThisDate(Connection connection, int trainId, String selectedDate) throws SQLException {
        int availableSeats = 0;
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.GET_AVAILABLE_SEATS);
        pstmt.setInt(1, trainId);
        pstmt.setString(2, selectedDate);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            availableSeats = rs.getInt("available_seats");
        }
        return availableSeats;
    }

    @Override
    public void changeTrainAvailableSeatsOnThisDate(Connection connection, int trainId, String selectedDate, int availableSeatsNow) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.CHANGE_AVAILABLE_SEATS);
        pstmt.setInt(1, availableSeatsNow);
        pstmt.setInt(2, trainId);
        pstmt.setString(3, selectedDate);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to change train available seats on this date");
        }
    }
}
