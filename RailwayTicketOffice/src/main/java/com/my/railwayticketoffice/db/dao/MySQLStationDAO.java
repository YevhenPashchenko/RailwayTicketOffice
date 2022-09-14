package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements {@link StationDAO} interface methods for MySQL.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLStationDAO implements StationDAO {

    @Override
    public List<Station> getStations(Connection connection, String locale) throws SQLException {
        List<Station> stations = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs;
        if ("en".equals(locale)) {
            rs = stmt.executeQuery(MySQLStationDAOQuery.GET_EN_STATIONS);
        } else {
            rs = stmt.executeQuery(MySQLStationDAOQuery.GET_STATIONS);
        }
        while (rs.next()) {
            Station station = new Station();
            station.setId(rs.getInt("id"));
            station.setName(rs.getString("name"));
            stations.add(station);
        }
        return stations;
    }

    @Override
    public int checkIfStationExists(Connection connection, String stationName, String locale) throws SQLException {
        int id = 0;
        PreparedStatement pstmt;
        if ("en".equals(locale)) {
            pstmt = connection.prepareStatement(MySQLStationDAOQuery.CHECK_IF_STATION_EN_EXISTS);
        } else {
            pstmt = connection.prepareStatement(MySQLStationDAOQuery.CHECK_IF_STATION_EXISTS);
        }
        pstmt.setString(1, stationName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
        return id;
    }

    @Override
    public int addStation(Connection connection, String stationName) throws SQLException {
        int id;
        PreparedStatement pstmt = connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, stationName);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to add station to database");
        }
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getInt(1);
        } else {
            throw new SQLException("Failed to add station to database");
        }
        return id;
    }

    @Override
    public void addStationEN(Connection connection, int stationId, String stationNameEN) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION_EN);
        pstmt.setInt(1, stationId);
        pstmt.setString(2, stationNameEN);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to add station to database");
        }
    }

    @Override
    public void deleteStation(Connection connection, int stationId) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLStationDAOQuery.DELETE_STATION);
        pstmt.setInt(1, stationId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to delete station from database");
        }
    }

    @Override
    public void editStation(Connection connection, int stationId, String stationName, String locale) throws SQLException {
        PreparedStatement pstmt;
        if ("en".equals(locale)) {
            pstmt = connection.prepareStatement(MySQLStationDAOQuery.EDIT_EN_STATION);
        } else {
            pstmt = connection.prepareStatement(MySQLStationDAOQuery.EDIT_STATION);
        }
        pstmt.setString(1, stationName);
        pstmt.setInt(2, stationId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit station data in database");
        }
    }
}
