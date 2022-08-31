package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class that implements {@link TrainDAO} interface methods for MySQL.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLTrainDAO implements TrainDAO {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

    @Override
    public List<Train> getAllTrains(Connection connection) throws SQLException {
        List<Train> trains = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(MySQLTrainDAOQuery.GET_ALL_TRAINS_DATA);
        while (rs.next()) {
            Train train = new Train();
            train.setId(rs.getInt("id"));
            train.setNumber(rs.getString("number"));
            train.setSeats(rs.getInt("seats"));
            train.setDepartureTime(LocalTime.parse(rs.getString("departure_time"), formatter));
            trains.add(train);
        }
        return trains;
    }

    @Override
    public List<Train> getTrainsSpecifiedByStationsAndDate(Connection connection, int fromStationId, int toStationId, String formattedDate) throws SQLException {
        List<Train> trains = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE);
        pstmt.setInt(1, fromStationId);
        pstmt.setInt(2, toStationId);
        pstmt.setString(3, formattedDate);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Train train = new Train();
            train.setId(rs.getInt("id"));
            train.setNumber(rs.getString("number"));
            train.setSeats(rs.getInt("available_seats"));
            train.setDepartureTime(LocalTime.parse(rs.getString("departure_time"), formatter));
            trains.add(train);
        }
        return trains;
    }

    @Override
    public void getRoutesForTrains(Connection connection, List<Train> trains, String locale) throws SQLException {
        int count = 1;
        StringBuilder query = new StringBuilder();
        query.append(MySQLTrainDAOQuery.GET_ROUTES_FOR_TRAINS);
        if ("en".equals(locale)) {
            query.append(MySQLTrainDAOQuery.GET_STATION_EN_NAME);
        } else {
            query.append(MySQLTrainDAOQuery.GET_STATION_NAME);
        }
        query.append("(?");
        for (int i = 1; i < trains.size(); i++) {
            query.append(", ?");
        }
        query.append(") ")
                .append(MySQLTrainDAOQuery.ORDER_BY);
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (Train train:
             trains) {
            pstmt.setInt(count++, train.getId());
        }
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Station station = new Station();
            int trainId = rs.getInt("train_id");
            Train train = new Train();
            for (Train value : trains) {
                if (value.getId() == trainId) {
                    train = value;
                }
            }
            addRouteToTrain(train, rs, station);
        }
    }

    private void addRouteToTrain(Train train, ResultSet rs, Station station) throws SQLException {
        station.setId(rs.getInt("station_id"));
        station.setName(rs.getString("name"));
        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), rs.getString("time_since_start"));
        train.getRoute().addStopTime(station.getId(), LocalTime.parse(rs.getString("stop_time"), formatter));
        train.getRoute().addDistanceFromStart(station.getId(), rs.getInt("distance_from_start"));
    }

    @Override
    public Train getTrainThatIsInSchedule(Connection connection, int trainId) throws SQLException {
        Train train = new Train();
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAIN_THAT_IS_IN_SCHEDULE);
        pstmt.setInt(1, trainId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            train.setId(trainId);
            train.setNumber(rs.getString("number"));
            train.setSeats(rs.getInt("available_seats"));
            train.setDepartureTime(LocalTime.parse(rs.getString("departure_time"), formatter));
        }
        return train;
    }

    @Override
    public void addTrain(Connection connection, String trainNumber, int trainSeats, String trainDepartureTime) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.ADD_TRAIN);
        pstmt.setString(1, trainNumber);
        pstmt.setInt(2, trainSeats);
        pstmt.setString(3, trainDepartureTime);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to add new train in database");
        }
    }

    @Override
    public void deleteTrain(Connection connection, int trainId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.DELETE_TRAIN);
        pstmt.setInt(1, trainId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to delete train from database");
        }
    }

    @Override
    public void editTrain(Connection connection, Train train) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.EDIT_TRAIN);
        pstmt.setString(1, train.getNumber());
        pstmt.setInt(2, train.getSeats());
        pstmt.setObject(3, train.getDepartureTime());
        pstmt.setInt(4, train.getId());
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit train data in database");
        }
    }

    @Override
    public void deleteStationFromTrainRoute(Connection connection, int trainId, int stationId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.DELETE_STATION_FROM_TRAIN_ROUTE);
        pstmt.setInt(1, trainId);
        pstmt.setInt(2, stationId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to delete station from train route");
        }
    }

    @Override
    public void addStationToTrainRoute(Connection connection, String timeSinceStart, String stopTime, int distanceFromStart, int trainId, int stationId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.ADD_STATION_TO_TRAIN_ROUTE);
        pstmt.setString(1, timeSinceStart);
        pstmt.setString(2, stopTime);
        pstmt.setInt(3, distanceFromStart);
        pstmt.setInt(4, trainId);
        pstmt.setInt(5, stationId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to add station to train route");
        }
    }

    @Override
    public void editStationDataOnTrainRoute(Connection connection, String timeSinceStart, String stopTime, int distanceFromStart, int trainId, int stationId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.EDIT_STATION_DATA_ON_TRAIN_ROUTE);
        pstmt.setString(1, timeSinceStart);
        pstmt.setString(2, stopTime);
        pstmt.setInt(3, distanceFromStart);
        pstmt.setInt(4, trainId);
        pstmt.setInt(5, stationId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit station data on train route");
        }
    }

    @Override
    public Train getTrain(Connection connection, int trainId) throws SQLException {
        Train train = new Train();
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAIN);
        pstmt.setInt(1, trainId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            train.setId(trainId);
            train.setNumber(rs.getString("number"));
            train.setSeats(rs.getInt("seats"));
            train.setDepartureTime(LocalTime.parse(rs.getString("departure_time"), formatter));
        }
        return train;
    }
}
