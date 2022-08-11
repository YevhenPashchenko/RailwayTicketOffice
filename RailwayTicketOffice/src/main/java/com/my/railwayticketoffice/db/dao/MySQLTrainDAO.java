package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}
