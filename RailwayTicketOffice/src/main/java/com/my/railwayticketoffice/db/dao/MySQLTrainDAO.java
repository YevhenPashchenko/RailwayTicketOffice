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

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);

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
    public Map<Integer, Train> getTrainsSpecifiedByStationsAndDate(Connection connection, int fromStationId, int toStationId, String formattedDate) throws SQLException {
        Map<Integer, Train> trains = new HashMap<>();
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
            trains.put(train.getId(), train);
        }
        return trains;
    }

    @Override
    public void getRoutesForTrains(Connection connection, Map<Integer, Train> trains) throws SQLException {
        int count = 1;
        StringBuilder query = new StringBuilder();
        query.append(MySQLTrainDAOQuery.GET_ROUTES_FOR_TRAINS)
                .append("(?");
        for (int i = 1; i < trains.size(); i++) {
            query.append(", ?");
        }
        query.append(")");
        PreparedStatement pstmt = connection.prepareStatement(query.toString());
        for (Integer id:
             trains.keySet()) {
            pstmt.setInt(count++, id);
        }
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Station station = new Station();
            Map<Station, LocalTime> stationAndStopTime = new HashMap<>();
            Map<LocalTime, Map<Station, LocalTime>> timeSinceStartAndMap = new HashMap<>();
            Train train = trains.get(rs.getInt("train_id"));
            Map<Integer, Map<LocalTime, Map<Station, LocalTime>>> route = train.getRoute();
            LocalTime timeSinceStart = LocalTime.parse(rs.getString("time_since_start"), formatter);
            LocalTime stopTime = LocalTime.parse(rs.getString("stop_time"), formatter);
            int distanceFromStart = rs.getInt("distance_from_start");
            station.setId(rs.getInt("station_id"));
            station.setName(rs.getString("name"));
            stationAndStopTime.put(station, stopTime);
            timeSinceStartAndMap.put(timeSinceStart, stationAndStopTime);
            route.put(distanceFromStart, timeSinceStartAndMap);
            train.setRoute(route);
            trains.put(train.getId(), train);
        }
    }
}
