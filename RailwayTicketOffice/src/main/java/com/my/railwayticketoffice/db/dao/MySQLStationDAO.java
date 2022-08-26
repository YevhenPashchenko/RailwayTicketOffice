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
    public List<Station> getStations(Connection connection) throws SQLException {
        List<Station> stations = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(MySQLStationDAOQuery.GET_STATIONS);
        while (rs.next()) {
            Station station = new Station();
            station.setId(rs.getInt("id"));
            station.setName(rs.getString("name"));
            stations.add(station);
        }
        return stations;
    }

    @Override
    public void addStation(Connection connection, String stationName) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION);
        pstmt.setString(1, stationName);
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
    public void editStation(Connection connection, int stationId, String stationName) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(MySQLStationDAOQuery.EDIT_STATION);
        pstmt.setString(1, stationName);
        pstmt.setInt(2, stationId);
        int affectedRow = pstmt.executeUpdate();
        if (affectedRow == 0) {
            throw new SQLException("Failed to edit station data in database");
        }
    }
}
