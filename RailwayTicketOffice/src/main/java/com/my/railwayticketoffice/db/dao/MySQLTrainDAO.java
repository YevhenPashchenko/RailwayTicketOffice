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
        ResultSet rs = stmt.executeQuery(MySQLTrainDAOQuery.GET_ALL_TRAINS);
        while (rs.next()) {
            Train train = new Train();
            train.setId(rs.getInt("id"));
            train.setNumber(rs.getString("number"));
            train.setDepartureTime(LocalTime.parse(rs.getString("departure_time"), formatter));
            trains.add(train);
        }
        return trains;
    }

    @Override
    public void getCarriagesForTrains(Connection connection, List<Train> trains) throws SQLException {
        Map<Integer, Train> trainsMap = new HashMap<>();
        int count = 1;
        StringBuilder query = new StringBuilder(MySQLTrainDAOQuery.GET_CARRIAGES_FOR_TRAINS);
        query.append("(?");
        for (int i = 1; i < trains.size(); i++) {
            query.append(", ?");
        }
        query.append(")");
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (Train train:
             trains) {
            pstmt.setInt(count++, train.getId());
            trainsMap.put(train.getId(), train);
        }
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int trainId = rs.getInt("train_id");
            Train.Carriage carriage = trainsMap.get(trainId).new Carriage();
            carriage.setId(rs.getInt("id"));
            carriage.setNumber(rs.getInt("number"));
            carriage.setType(rs.getString("type"));
            carriage.setMaxSeats(rs.getInt("max_seats"));
            trainsMap.get(trainId).addCarriage(carriage.getId(), carriage);
        }
    }

    @Override
    public List<Train> getTrainsSpecifiedByStationsAndDate(Connection connection, int fromStationId, int toStationId, String date) throws SQLException {
        List<Train> trains = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE);
        pstmt.setInt(1, fromStationId);
        pstmt.setInt(2, toStationId);
        pstmt.setString(3, date);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Train train = new Train();
            train.setId(rs.getInt("id"));
            train.setNumber(rs.getString("number"));
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

    @Override
    public void getFreeSeatsForTrainsByDate(Connection connection, List<Train> trains, String date) throws SQLException {
        int count = 1;
        StringBuilder query = new StringBuilder(MySQLTrainDAOQuery.GET_FREE_SEATS_FOR_TRAINS_BY_DATE)
                .append("(?");
        for (int i = 1; i < trains.size(); i++) {
            query.append(", ?");
        }
        query.append(")");
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        pstmt.setString(count++, date);
        for (Train train:
             trains) {
            pstmt.setInt(count++, train.getId());
        }
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Train train = null;
            int trainId = rs.getInt("train_id");
            int carriageId = rs.getInt("carriage_id");
            int seatNumber = rs.getInt("seat_number");
            for (Train t:
                 trains) {
                if (trainId == t.getId()) {
                    train = t;
                    break;
                }
            }
            if (train == null) {
                throw new SQLException("No train to add seat number");
            }
            train.getCarriages().get(carriageId).addSeat(seatNumber);
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
    public int checkIfTrainExists(Connection connection, String trainNumber) throws SQLException {
        int id = 0;
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_TRAIN_EXISTS);
        pstmt.setString(1, trainNumber);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

    @Override
    public void addTrain(Connection connection, String trainNumber, String trainDepartureTime) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.ADD_TRAIN);
        pstmt.setString(1, trainNumber);
        pstmt.setString(2, trainDepartureTime);
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
        pstmt.setObject(2, train.getDepartureTime());
        pstmt.setInt(3, train.getId());
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit train data in database");
        }
    }

    @Override
    public Map<Integer, String> getCarriagesTypes(Connection connection) throws SQLException {
        Map<Integer, String> carriagesTypes = new HashMap<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(MySQLTrainDAOQuery.GET_CARRIAGES_TYPES);
        while (rs.next()) {
            int typeId = rs.getInt("id");
            String type = rs.getString("type");
            carriagesTypes.put(typeId, type);
        }
        return carriagesTypes;
    }

    @Override
    public int checkIfTrainHasCarriageWithThisNumber(Connection connection, int trainId, int carriageNumber) throws SQLException {
        int id = 0;
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_TRAIN_HAS_CARRIAGE_WITH_THIS_NUMBER);
        pstmt.setInt(1, trainId);
        pstmt.setInt(2, carriageNumber);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

    @Override
    public int checkIfCarriageExists(Connection connection, int carriageNumber, int typeId) throws SQLException {
        int id = 0;
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_CARRIAGE_EXISTS);
        pstmt.setInt(1, carriageNumber);
        pstmt.setInt(2, typeId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

    @Override
    public int createCarriage(Connection connection, int carriageNumber, int typeId) throws SQLException {
        int id;
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.CREATE_CARRIAGE, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, carriageNumber);
        pstmt.setInt(2, typeId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to create new carriage database");
        }
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        } else {
            throw new SQLException("Failed to create new carriage database");
        }
        return id;
    }

    @Override
    public void addCarriageToTrain(Connection connection, int trainId, int carriageId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.ADD_CARRIAGE_TO_TRAIN);
        pstmt.setInt(1, trainId);
        pstmt.setInt(2, carriageId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to add carriage to train");
        }
    }

    @Override
    public int getCarriageMaxSeats(Connection connection, int carriageId) throws SQLException {
        int max_seats;
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.GET_CARRIAGE_MAX_SEATS);
        pstmt.setInt(1, carriageId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            max_seats = rs.getInt("max_seats");
        } else {
            throw new SQLException("Failed to get carriage maximum seats");
        }
        return max_seats;
    }

    @Override
    public void deleteCarriageFromTrain(Connection connection, int trainId, int carriageId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.DELETE_CARRIAGE_FROM_TRAIN);
        pstmt.setInt(1, trainId);
        pstmt.setInt(2, carriageId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to delete carriage from train");
        }
    }

    @Override
    public int checkIfCarriageTypeExists(Connection connection, String carriageType) throws SQLException {
        int id = 0;
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_CARRIAGE_TYPE_EXISTS);
        pstmt.setString(1, carriageType);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

    @Override
    public void addCarriageType(Connection connection, String carriageType, int maxSeats) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.ADD_CARRIAGE_TYPE);
        pstmt.setString(1, carriageType);
        pstmt.setInt(2, maxSeats);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to add new carriage type to database");
        }
    }

    @Override
    public void deleteCarriageType(Connection connection, int typeId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.DELETE_CARRIAGE_TYPE);
        pstmt.setInt(1, typeId);
        int deletedRow = pstmt.executeUpdate();
        if (deletedRow == 0) {
            throw new SQLException("Failed to delete carriage type from database");
        }
    }

    @Override
    public void editCarriageType(Connection connection, String newCarriageType, int typeId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.EDIT_CARRIAGE_TYPE);
        pstmt.setString(1, newCarriageType);
        pstmt.setInt(2, typeId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit carriage type");
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
            train.setDepartureTime(LocalTime.parse(rs.getString("departure_time"), formatter));
        }
        return train;
    }
}
