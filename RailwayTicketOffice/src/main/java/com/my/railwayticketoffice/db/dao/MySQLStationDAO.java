package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Station;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
