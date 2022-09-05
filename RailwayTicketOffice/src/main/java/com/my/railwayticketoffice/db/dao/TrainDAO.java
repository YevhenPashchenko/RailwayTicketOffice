package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.entity.Train;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interface for managing train data.
 *
 * @author Yevhen Pashchenko
 */
public interface TrainDAO {

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return all {@link Train}.
     * @param connection Connection object.
     * @return a list of {@link Train}.
     * @throws SQLException if a database access error occurs.
     */
    List<Train> getAllTrains(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be added {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * to all {@link Train} from the list.
     * @param connection Connection object.
     * @param trains list of {@link Train}.
     * @throws SQLException if a database access error occurs.
     */
    void getCarriagesForTrains(Connection connection, List<Train> trains) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return
     * list of {@link com.my.railwayticketoffice.entity.Train} traveling through the station specified in the parameters of this method
     * in the date specified in the parameters of this method.
     * @param connection - Connection object.
     * @param fromStationId - departure station id.
     * @param toStationId - destination station id.
     * @param date - date of departure.
     * @return list of eligible {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException – if a database access error occurs.
     */
    List<Train> getTrainsSpecifiedByStationsAndDate(Connection connection, int fromStationId, int toStationId, String date) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be added route to each
     * {@link com.my.railwayticketoffice.entity.Train} from the list that is passed as a method parameter.
     * @param connection Connection object.
     * @param trains list of {@link Train}.
     * @param locale current locale.
     * @throws SQLException – if a database access error occurs.
     */
    void getRoutesForTrains(Connection connection, List<Train> trains, String locale) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should add free seats to each {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * in each {@link com.my.railwayticketoffice.entity.Train} from the list.
     * @param connection Connection object.
     * @param trains list of {@link Train}.
     * @param date search date.
     * @throws SQLException if a database access error occurs.
     */
    void getFreeSeatsForTrainsByDate(Connection connection, List<Train> trains, String date) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return {@link com.my.railwayticketoffice.entity.Train} id if
     * it with this number exists.
     * @param connection Connection object.
     * @param trainNumber {@link Train} number.
     * @return {@link com.my.railwayticketoffice.entity.Train} id if it with this number exists or 0 if not.
     * @throws SQLException if a database access error occurs.
     */
    int checkIfTrainExists(Connection connection, String trainNumber) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should add new {@link Train} to database.
     * @param connection Connection object.
     * @param trainNumber number of the new train.
     * @param trainDepartureTime departure time of the new train. Must match the pattern "hh:mm".
     * @throws SQLException if a database access error occurs.
     */
    void addTrain(Connection connection, String trainNumber, String trainDepartureTime) throws SQLException;

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
     * When a class implementing interface {@link TrainDAO} call this method should be return map where carriage type is value and carriage type id is keys.
     * @param connection Connection object.
     * @return map where carriage type is value and carriage type id is keys.
     * @throws SQLException if a database access error occurs.
     */
    Map<Integer, String> getCarriagesTypes(Connection connection) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return {@link com.my.railwayticketoffice.entity.Train.Carriage} id if
     * {@link Train} has it with this number.
     * @param connection Connection object.
     * @param carriageNumber {@link Train.Carriage} number.
     * @return {@link com.my.railwayticketoffice.entity.Train.Carriage} id if {@link Train} has it with this number exists or 0 if not.
     * @throws SQLException if a database access error occurs.
     */
    int checkIfTrainHasCarriageWithThisNumber(Connection connection, int TrainId, int carriageNumber) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should be return {@link com.my.railwayticketoffice.entity.Train.Carriage} id if
     * it with this number and type exists.
     * @param connection Connection object.
     * @param carriageNumber {@link Train.Carriage} number.
     * @param typeId {@link Train.Carriage} type id.
     * @return {@link com.my.railwayticketoffice.entity.Train.Carriage} id if it with this number exists or 0 if not.
     * @throws SQLException if a database access error occurs.
     */
    int checkIfCarriageExists(Connection connection, int carriageNumber, int typeId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should create new {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * in database.
     * @param connection Connection object.
     * @param carriageNumber new {@link Train.Carriage} number.
     * @param typeId new {@link Train.Carriage} type id.
     * @return new {@link Train.Carriage} id.
     * @throws SQLException if a database access error occurs.
     */
    int createCarriage(Connection connection, int carriageNumber, int typeId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should add new {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * to {@link Train}.
     * @param connection Connection object.
     * @param trainId {@link Train} id.
     * @param carriageId {@link com.my.railwayticketoffice.entity.Train.Carriage} id.
     * @throws SQLException if a database access error occurs.
     */
    void addCarriageToTrain(Connection connection, int trainId, int carriageId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should return the maximum number
     * of {@link com.my.railwayticketoffice.entity.Train.Carriage} seats.
     * @param connection Connection object.
     * @param carriageId {@link com.my.railwayticketoffice.entity.Train.Carriage} id.
     * @return maximum number of {@link com.my.railwayticketoffice.entity.Train.Carriage} seats.
     * @throws SQLException if a database access error occurs.
     */
    int getCarriageMaxSeats(Connection connection, int carriageId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should delete {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * from {@link Train}.
     * @param connection Connection object.
     * @param trainId {@link Train} id.
     * @param carriageId {@link com.my.railwayticketoffice.entity.Train.Carriage} id.
     * @throws SQLException if a database access error occurs.
     */
    void deleteCarriageFromTrain(Connection connection, int trainId, int carriageId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should check that {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * type exists in database.
     * @param connection Connection object.
     * @param carriageType {@link com.my.railwayticketoffice.entity.Train.Carriage} type.
     * @return {@link com.my.railwayticketoffice.entity.Train.Carriage} id if it exists or 0 if not.
     * @throws SQLException if a database access error occurs.
     */
    int checkIfCarriageTypeExists(Connection connection, String carriageType) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should edit {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * number in {@link Train}.
     * @param connection Connection object.
     * @param newCarriageId {@link Train.Carriage} id.
     * @param trainId {@link Train} id.
     * @param carriageId {@link Train.Carriage} id.
     * @throws SQLException if a database access error occurs.
     */
    void editCarriageNumberInTrain(Connection connection, int newCarriageId, int trainId, int carriageId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should add new {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * type to database.
     * @param connection Connection object.
     * @param carriageType {@link com.my.railwayticketoffice.entity.Train.Carriage} type.
     * @param maxSeats maximum number seats in this {@link com.my.railwayticketoffice.entity.Train.Carriage} type.
     * @throws SQLException if a database access error occurs.
     */
    void addCarriageType(Connection connection, String carriageType, int maxSeats) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should delete {@link com.my.railwayticketoffice.entity.Train.Carriage}
     * type from database.
     * @param connection Connection object.
     * @param typeId {@link Train.Carriage} type id.
     * @throws SQLException if a database access error occurs.
     */
    void deleteCarriageType(Connection connection, int typeId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should edit {@link com.my.railwayticketoffice.entity.Train.Carriage} type.
     * @param connection Connection object.
     * @param typeId {@link Train.Carriage} type id.
     * @param newCarriageType new {@link com.my.railwayticketoffice.entity.Train.Carriage} type.
     * @throws SQLException if a database access error occurs.
     */
    void editCarriageType(Connection connection, String newCarriageType, int typeId) throws SQLException;

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
     * @param connection Connection object.
     * @param timeSinceStart time from the moment the train departs from the first station of the route.
     * @param stopTime train stop time at the route station.
     * @param distanceFromStart distance from the first station of the train route.
     * @param trainId train id.
     * @param stationId id station.
     * @throws SQLException if a database access error occurs.
     */
    void editStationDataOnTrainRoute(Connection connection, String timeSinceStart, String stopTime, int distanceFromStart, int trainId, int stationId) throws SQLException;

    /**
     * When a class implementing interface {@link TrainDAO} call this method should return {@link com.my.railwayticketoffice.entity.Train}.
     * @param connection Connection object.
     * @param trainId train id.
     * @return {@link com.my.railwayticketoffice.entity.Train}.
     * @throws SQLException if a database access error occurs.
     */
    Train getTrain(Connection connection, int trainId) throws SQLException;
}
