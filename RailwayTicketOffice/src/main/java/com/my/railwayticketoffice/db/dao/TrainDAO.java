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
    List<Train> getAllTrains(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return
     * list of {@link com.my.railwayticketoffice.entity.Train} traveling through the station specified in the parameters of this method
     * in the date specified in the parameters of this method.
     * @param connection - Connection object.
     * @param fromStationId - departure station id.
     * @param toStationId - destination station id.
     * @param formattedDate - date of departure.
     * @return list of eligible {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException – if a database access error occurs.
     */
    List<Train> getTrainsSpecifiedByStationsAndDate(Connection connection, int fromStationId, int toStationId, String formattedDate) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be added route to each
     * {@link com.my.railwayticketoffice.entity.Train} from the list that is passed as a method parameter.
     * @param connection - Connection object.
     * @param trains - list of {@link Train}.
     * @throws SQLException – if a database access error occurs.
     */
    void getRoutesForTrains(Connection connection, List<Train> trains) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return {@link com.my.railwayticketoffice.entity.Train}
     * if it is in schedule.
     * @param connection - Connection object.
     * @param trainId - {@link com.my.railwayticketoffice.entity.Train} id.
     * @return {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException – if a database access error occurs.
     */
    Train getTrainThatIsInSchedule(Connection connection, int trainId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be added route to
     * {@link com.my.railwayticketoffice.entity.Train} that is passed as a method parameter.
     * @param connection - Connection object.
     * @param train - {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException – if a database access error occurs.
     */
    void getRouteForTrain(Connection connection, Train train) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be added new {@link Train} to database.
     * @param connection - Connection object.
     * @param trainNumber - number of the new train.
     * @param trainSeats - number of seats of the new train.
     * @param trainDepartureTime - departure time of the new train. Must match the pattern "hh:mm" or "hh:mm:ss".
     * @throws SQLException – if a database access error occurs.
     */
    void addTrain(Connection connection, String trainNumber, int trainSeats, String trainDepartureTime) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should delete {@link Train} from database.
     * @param connection - Connection object.
     * @param trainId - number of deleted train.
     * @throws SQLException – if a database access error occurs.
     */
    void deleteTrain(Connection connection, int trainId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should edit {@link Train} data in database.
     * @param connection - Connection object.
     * @param train - edited {@link Train}.
     * @throws SQLException – if a database access error occurs.
     */
    void editTrain(Connection connection, Train train) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should delete station from train route.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @param stationId - id station that must be deleted from train route.
     * @throws SQLException – if a database access error occurs.
     */
    void deleteStationFromTrainRoute(Connection connection, int trainId, int stationId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should add station to train route.
     * @param connection - Connection object.
     * @param timeSinceStart - time from the moment the train departs from the first station of the route.
     * @param stopTime - train stop time at the route station.
     * @param distanceFromStart - distance from the first station of the train route.
     * @param trainId - train id.
     * @param stationId - id station that must be added to train route.
     * @throws SQLException – if a database access error occurs.
     */
    void addStationToTrainRoute(Connection connection, String timeSinceStart, String stopTime, int distanceFromStart, int trainId, int stationId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should edit station data on train route.
     * @param connection - Connection object.
     * @param timeSinceStart - time from the moment the train departs from the first station of the route.
     * @param stopTime - train stop time at the route station.
     * @param distanceFromStart - distance from the first station of the train route.
     * @param trainId - train id.
     * @param stationId - id station.
     * @throws SQLException – if a database access error occurs.
     */
    void editStationDataOnTrainRoute(Connection connection, String timeSinceStart, String stopTime, int distanceFromStart, int trainId, int stationId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return {@link com.my.railwayticketoffice.entity.Train}.
     * @param connection - Connection object.
     * @param trainId - train id.
     * @return {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException – if a database access error occurs.
     */
    Train getTrain(Connection connection, int trainId) throws SQLException;
}
