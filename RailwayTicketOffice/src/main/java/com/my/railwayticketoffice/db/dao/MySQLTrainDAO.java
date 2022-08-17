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
    public List<Train> getAllTrainsDataForSchedule(Connection connection) throws SQLException {
        List<Train> trains = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(MySQLTrainDAOQuery.GET_ALL_TRAINS_DATA_FOR_SCHEDULE);
        while (rs.next()) {
            Train train = new Train();
            train.setId(rs.getInt("id"));
            train.setSeats(rs.getInt("seats"));
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
    public void getRoutesForTrains(Connection connection, List<Train> trains) throws SQLException {
        int count = 1;
        StringBuilder query = new StringBuilder();
        query.append(MySQLTrainDAOQuery.GET_ROUTES_FOR_TRAINS)
                .append("(?");
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
            int index = 0;
            Train train = new Train();
            for (int i = 0; i < trains.size(); i++) {
                if (trains.get(i).getId() == trainId) {
                    index = i;
                    train = trains.get(i);
                }
            }
            addRouteToTrain(train, rs, station);
            trains.set(index, train);
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
            train.setSeats(rs.getInt("available_seats"));
        }
        return train;
    }

    @Override
    public void getRouteForTrain(Connection connection, Train train) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLTrainDAOQuery.GET_ROUTE_FOR_TRAIN);
        pstmt.setInt(1, train.getId());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Station station = new Station();
            addRouteToTrain(train, rs, station);
        }
    }

    private void addRouteToTrain(Train train, ResultSet rs, Station station) throws SQLException {
        station.setId(rs.getInt("station_id"));
        station.setName(rs.getString("name"));
        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), LocalTime.parse(rs.getString("time_since_start"), formatter));
        train.getRoute().addStopTime(station.getId(), LocalTime.parse(rs.getString("stop_time"), formatter));
        train.getRoute().addDistanceFromStart(station.getId(), rs.getInt("distance_from_start"));
    }
}
