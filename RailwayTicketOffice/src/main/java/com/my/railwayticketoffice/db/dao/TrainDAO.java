package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing data in database that related to train data.
 *
 * @author Yevhen Pashchenko
 */
public interface TrainDAO {

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return a data
     * that related to train schedule data.
     * @param connection - Connection object.
     * @throws SQLException – if a database access error occurs.
     * @return a list of {@link Train}
     */
    List<Train> getAllTrainsDataForSchedule(Connection connection) throws SQLException;
}
