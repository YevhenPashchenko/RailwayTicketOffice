package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Station;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing station data.
 *
 * @author Yevhen Pashchenko
 */
public interface StationDAO {

    /**
     * When a class implementing interface {@link StationDAO} call this method should be return a list of stations.
     * @param connection - Connection object.
     * @throws SQLException – if a database access error occurs.
     * @return a list of {@link Station}.
     */
    List<Station> getStations(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should be added stations to database.
     * @param connection - Connection object.
     * @param stationName - station name.
     * @throws SQLException – if a database access error occurs.
     */
    void addStation(Connection connection, String stationName) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should be deleted stations from database.
     * @param connection - Connection object.
     * @param stationId - station id.
     * @throws SQLException – if a database access error occurs.
     */
    void deleteStation(Connection connection, int stationId) throws SQLException;
}
