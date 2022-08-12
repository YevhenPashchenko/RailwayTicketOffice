package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements {@link TrainDAO} interface methods for MySQL.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLTrainDAO implements TrainDAO {

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
        return null;
    }
}
