package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing train data.
 *
 * @author Yevhen Pashchenko
 */
public interface TrainDAO {

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return a train data
     * for train schedule data.
     * @param connection - Connection object.
     * @return a list of {@link Train}
     * @throws SQLException – if a database access error occurs.
     */
    List<Train> getAllTrainsDataForSchedule(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return
     * list of {@link com.my.railwayticketoffice.entity.Train} traveling through the station specified
     * in the parameters of this method in the date specified in the parameters of this method.
     * @param connection - Connection object.
     * @param fromStationId - departure station id.
     * @param toStationId - destination station id.
     * @param formattedDate - date of departure.
     * @return list of eligible {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException– if a database access error occurs.
     */
    List<Train> getTrainsSpecifiedByStationsAndDate(Connection connection, int fromStationId, int toStationId, String formattedDate) throws SQLException;
}
