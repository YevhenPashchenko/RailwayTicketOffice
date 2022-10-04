package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;

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
    public void addData(Connection connection, List<String> scheduleDates, List<Train> trains, User user) throws SQLException {
        int countValues = 0;
        int count = 1;
        for (int i = 0; i < scheduleDates.size(); i++) {
            for (Train train:
                    trains) {
                for (Train.Carriage carriage:
                        train.getCarriages().values()) {
                    for (int j = 0; j < carriage.getMaxSeats(); j++) {
                        countValues++;
                    }
                }
            }
        }
        StringBuilder query = new StringBuilder(MySQLScheduleDAOQuery.ADD_DATA);
        query.append(MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA);
        for (int i = countValues; i > 1; i--) {
            query.append(", ")
                    .append(MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA);
        }
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (String scheduleDate:
             scheduleDates) {
            for (Train train:
                 trains) {
                for (Train.Carriage carriage:
                     train.getCarriages().values()) {
                    for (int i = 1; i <= carriage.getMaxSeats(); i++) {
                        pstmt.setString(count++, scheduleDate);
                        pstmt.setInt(count++, train.getId());
                        pstmt.setInt(count++, carriage.getId());
                        pstmt.setInt(count++, i);
                        if (user != null) {
                            pstmt.setInt(count++, user.getId());
                        } else {
                            pstmt.setObject(count++, null);
                        }
                    }
                }
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
            throw new SQLException("Data less than current day not deleted from train schedule");
        }
    }

    @Override
    public int getTrainAvailableSeatsOnThisDate(Connection connection, int trainId, String selectedDate) throws SQLException {
        int availableSeats;
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.GET_AVAILABLE_SEATS);
        pstmt.setInt(1, trainId);
        pstmt.setString(2, selectedDate);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            availableSeats = rs.getInt("available_seats");
        } else {
            throw new SQLException("Failed to get train available seats");
        }
        return availableSeats;
    }

    @Override
    public boolean checkIfRecordExists(Connection connection, int trainId) throws SQLException {
        int result;
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_RECORD_EXISTS);
        pstmt.setInt(1, trainId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            result = rs.getInt("exist");
        } else {
            throw new SQLException("Failed to check if record exist");
        }
        return result > 0;
    }

    @Override
    public void deleteTrainFromScheduleAtDate(Connection connection, String departureDate, int trainId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_TRAIN_FROM_SCHEDULE);
        pstmt.setString(1, departureDate);
        pstmt.setInt(2, trainId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to delete train from schedule");
        }
    }

    @Override
    public void changeTrainAvailableSeatsOnThisDate(Connection connection, int userId, int trainId, String selectedDate, int carriageId, List<Integer> seatsNumbers) throws SQLException {
        int count = 1;
        StringBuilder query = new StringBuilder(MySQLScheduleDAOQuery.CHANGE_AVAILABLE_SEATS)
                .append("(?");
        for (int i = 1; i < seatsNumbers.size(); i++) {
            query.append(", ?");
        }
        query.append(")");
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        pstmt.setInt(count++, userId);
        pstmt.setString(count++, selectedDate);
        pstmt.setInt(count++, trainId);
        pstmt.setInt(count++, carriageId);
        for (Integer seatNumber:
                seatsNumbers) {
            pstmt.setInt(count++, seatNumber);
        }
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to change train available seats on this date");
        }
    }

    @Override
    public void editCarriageData(Connection connection, int newCarriageId, int trainId, int carriageId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.EDIT_CARRIAGE_DATA);
        pstmt.setInt(1, newCarriageId);
        pstmt.setInt(2, trainId);
        pstmt.setInt(3, carriageId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to delete train from schedule");
        }
    }

    @Override
    public Integer checkIfSeatBooked(Connection connection, String departureDate, int trainId, int carriageId, int seatNumber) throws SQLException {
        Integer userId = null;
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_SEAT_BOOKED);
        pstmt.setString(1, departureDate);
        pstmt.setInt(2, trainId);
        pstmt.setInt(3, carriageId);
        pstmt.setInt(4, seatNumber);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            userId = rs.getInt("user_id");
        }
        return userId;
    }

    @Override
    public void returnTicket(Connection connection, String departureDate, int trainId, int carriageId, int seatNumber) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.RETURN_TICKET);
        pstmt.setString(1, departureDate);
        pstmt.setInt(2, trainId);
        pstmt.setInt(3, carriageId);
        pstmt.setInt(4, seatNumber);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to return ticket");
        }
    }

    @Override
    public boolean checkIfCarriagesIsInSchedule(Connection connection, int typeId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_CARRIAGES_IS_IN_SCHEDULE);
        pstmt.setInt(1, typeId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        throw new SQLException("Failed to check if carriages is in schedule");
    }

    @Override
    public boolean checkIfTrainsThatHasThisStationOnTheRouteIsInSchedule(Connection connection, int stationId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_TRAINS_THAT_HAS_THIS_STATION_ON_THE_ROUTE_IS_IN_SCHEDULE);
        pstmt.setInt(1, stationId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        throw new SQLException("Failed to check if trains that has this station on the route is in schedule");
    }

    @Override
    public boolean checkIfTrainHasBookedSeatsAtDate(Connection connection, String departureDate, int trainId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_TRAIN_HAS_BOOKED_SEATS_AT_DATE);
        pstmt.setString(1, departureDate);
        pstmt.setInt(2, trainId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("count") > 0;
        }
        throw new SQLException("Failed to check if train has has booked seats at day");
    }
}
