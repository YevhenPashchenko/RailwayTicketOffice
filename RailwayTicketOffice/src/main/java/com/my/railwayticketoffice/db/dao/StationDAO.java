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
     * @param connection Connection object.
     * @param locale current locale.
     * @throws SQLException if a database access error occurs.
     * @return a list of {@link Station}.
     */
    List<Station> getStations(Connection connection, String locale) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should return an {@link Station} id if it
     * exists or 0 if not.
     * @param connection Connection object.
     * @param stationName {@link Station} name.
     * @param locale current locale.
     * @return an {@link Station} id if it exists or 0 if not.
     * @throws SQLException if a database access error occurs.
     */
    int checkIfStationExists(Connection connection, String stationName, String locale) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should be added station in Ukrainian to database
     * and return its id.
     * @param connection - Connection object.
     * @param stationName - station name in Ukrainian.
     * @throws SQLException – if a database access error occurs.
     * @return station id.
     */
    int addStation(Connection connection, String stationName) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should be added station in English.
     * @param connection - Connection object.
     * @param stationId - station id.
     * @param stationNameEN - station name in English.
     * @throws SQLException – if a database access error occurs.
     */
    void addStationEN(Connection connection, int stationId, String stationNameEN) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should be deleted station from database.
     * @param connection - Connection object.
     * @param stationId - station id.
     * @throws SQLException – if a database access error occurs.
     */
    void deleteStation(Connection connection, int stationId) throws SQLException;

    /**
     * When a class implementing interface {@link StationDAO} call this method should be edit station data in database.
     * @param connection - Connection object.
     * @param stationId - station id.
     * @param stationName - station name.
     * @param locale - current locale.
     * @throws SQLException – if a database access error occurs.
     */
    void editStation(Connection connection, int stationId, String stationName, String locale) throws SQLException;
}
