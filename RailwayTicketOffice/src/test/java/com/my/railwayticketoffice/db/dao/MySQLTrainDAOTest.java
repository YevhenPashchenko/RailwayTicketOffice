package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link MySQLTrainDAO}.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLTrainDAOTest {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ENGLISH);
    private final ResultSet rs = mock(ResultSet.class);
    private final Statement stmt = mock(Statement.class);
    private final PreparedStatement pstmt = mock(PreparedStatement.class);
    private final Connection connection = mock(Connection.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);

    @BeforeEach
    void beforeEach() throws Exception {
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
    }

    private Train getTrain(LocalTime time) {
        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));
        return train;
    }

    /**
     * Test for method getAllTrains from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetAllTrains() throws Exception {

        LocalTime time = LocalTime.now();

        Train train = getTrain(time);

        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLTrainDAOQuery.GET_ALL_TRAINS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getAllTrains(connection), Collections.singletonList(train));
    }

    /**
     * Test for method getCarriagesForTrains from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetCarriagesForTrains() throws Exception {
        LocalTime time = LocalTime.now();

        List<Train> trains = new ArrayList<>();
        Train train = getTrain(time);
        trains.add(train);

        List<Train> trainsWithCarriages = new ArrayList<>();
        Train trainWithCarriages = getTrain(time);
        Train.Carriage carriage = trainWithCarriages.new Carriage();
        carriage.setId(1);
        carriage.setNumber(1);
        carriage.setType("Тип");
        carriage.setMaxSeats(1);
        train.addCarriage(1, carriage);
        trainsWithCarriages.add(trainWithCarriages);

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_CARRIAGES_FOR_TRAINS + "(?)")).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("train_id")).thenReturn(1);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getInt("number")).thenReturn(1);
        when(rs.getString("type")).thenReturn("Тип");
        when(rs.getInt("max_seats")).thenReturn(1);

        DBManager.getInstance().getTrainDAO().getCarriagesForTrains(connection, trains);

        assertEquals(trains, trainsWithCarriages);
    }

    /**
     * Test for method getTrainsSpecifiedByStationsAndDate from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetTrainsSpecifiedByStationsAndDate() throws Exception {

        LocalTime time = LocalTime.now();

        int fromStationId = 1;
        int toStationId = 2;
        String formattedDate = time.format(formatter);

        Train train = getTrain(time);

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getTrainsSpecifiedByStationsAndDate(connection, fromStationId, toStationId, formattedDate), Collections.singletonList(train));
    }

    /**
     * Test for method getRoutesForTrains from {@link MySQLTrainDAO} with locale equals null.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetRoutesForTrains() throws Exception {

        LocalTime time = LocalTime.now();

        Train train = getTrain(time);

        Station station = new Station();
        station.setId(1);
        station.setName("Станція");

        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), time.format(formatter));
        train.getRoute().addStopTime(station.getId(), LocalTime.parse(time.format(formatter)));
        train.getRoute().addDistanceFromStart(station.getId(), 100);

        List<Train> trains = new ArrayList<>();
        Train train1 = getTrain(time);
        trains.add(train1);

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_ROUTES_FOR_TRAINS + MySQLTrainDAOQuery.GET_STATION_NAME + "(?) " + MySQLTrainDAOQuery.ORDER_BY)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("train_id")).thenReturn(1);
        when(rs.getInt("station_id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Станція");
        when(rs.getString("time_since_start")).thenReturn(time.format(formatter));
        when(rs.getString("stop_time")).thenReturn(time.format(formatter));
        when(rs.getInt("distance_from_start")).thenReturn(100);

        DBManager.getInstance().getTrainDAO().getRoutesForTrains(connection, trains, null);

        assertEquals(trains, Collections.singletonList(train));
    }

    /**
     * Test for method getRoutesForTrains from {@link MySQLTrainDAO} with locale equals uk.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetRoutesForTrainsLocaleUA() throws Exception {

        LocalTime time = LocalTime.now();
        String locale = "uk";

        Train train = getTrain(time);

        Station station = new Station();
        station.setId(1);
        station.setName("Станція");

        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), time.format(formatter));
        train.getRoute().addStopTime(station.getId(), LocalTime.parse(time.format(formatter)));
        train.getRoute().addDistanceFromStart(station.getId(), 100);

        List<Train> trains = new ArrayList<>();
        Train train1 = getTrain(time);
        trains.add(train1);

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_ROUTES_FOR_TRAINS + MySQLTrainDAOQuery.GET_STATION_NAME + "(?) " + MySQLTrainDAOQuery.ORDER_BY)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("train_id")).thenReturn(1);
        when(rs.getInt("station_id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Станція");
        when(rs.getString("time_since_start")).thenReturn(time.format(formatter));
        when(rs.getString("stop_time")).thenReturn(time.format(formatter));
        when(rs.getInt("distance_from_start")).thenReturn(100);

        DBManager.getInstance().getTrainDAO().getRoutesForTrains(connection, trains, locale);

        assertEquals(trains, Collections.singletonList(train));
    }

    /**
     * Test for method getRoutesForTrains from {@link MySQLTrainDAO} with locale equals en.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetRoutesForTrainsLocaleEN() throws Exception {

        LocalTime time = LocalTime.now();
        String locale = "en";

        Train train = getTrain(time);

        Station station = new Station();
        station.setId(1);
        station.setName("Station");

        train.getRoute().addStation(station);
        train.getRoute().addTimeSinceStart(station.getId(), time.format(formatter));
        train.getRoute().addStopTime(station.getId(), LocalTime.parse(time.format(formatter)));
        train.getRoute().addDistanceFromStart(station.getId(), 100);

        List<Train> trains = new ArrayList<>();
        Train train1 = getTrain(time);
        trains.add(train1);

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_ROUTES_FOR_TRAINS + MySQLTrainDAOQuery.GET_STATION_EN_NAME + "(?) " + MySQLTrainDAOQuery.ORDER_BY)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("train_id")).thenReturn(1);
        when(rs.getInt("station_id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Station");
        when(rs.getString("time_since_start")).thenReturn(time.format(formatter));
        when(rs.getString("stop_time")).thenReturn(time.format(formatter));
        when(rs.getInt("distance_from_start")).thenReturn(100);

        DBManager.getInstance().getTrainDAO().getRoutesForTrains(connection, trains, locale);

        assertEquals(trains, Collections.singletonList(train));
    }

    /**
     * Test for method getFreeSeatsForTrainsByDate from {@link MySQLTrainDAO}.
     *
     *  @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetFreeSeatsForTrainsByDate() throws Exception {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime time = LocalTime.now();

        List<Train> trains = new ArrayList<>();
        Train train = getTrain(time);
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(1);
        carriage.setNumber(1);
        carriage.setType("Тип");
        carriage.setMaxSeats(1);
        train.addCarriage(1, carriage);
        trains.add(train);

        List<Train> trainsWithSeats = new ArrayList<>();
        Train trainWithSeats = getTrain(time);
        Train.Carriage carriageWithSeats = train.new Carriage();
        carriage.setId(1);
        carriage.setNumber(1);
        carriage.setType("Тип");
        carriage.setMaxSeats(1);
        carriage.addSeat(1);
        trainWithSeats.addCarriage(1, carriageWithSeats);
        trainsWithSeats.add(trainWithSeats);

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_FREE_SEATS_FOR_TRAINS_BY_DATE + "(?)")).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("train_id")).thenReturn(1);
        when(rs.getInt("carriage_id")).thenReturn(1);
        when(rs.getInt("seat_number")).thenReturn(1);

        DBManager.getInstance().getTrainDAO().getFreeSeatsForTrainsByDate(connection, trains, date);
        assertEquals(trains, trainsWithSeats);
    }

    /**
     * Test for method checkIfTrainExists from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfTrainExists() throws Exception {
        int trainId = 1;
        String trainNumber = "Номер";

        when(connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_TRAIN_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(trainId);

        assertEquals(DBManager.getInstance().getTrainDAO().checkIfTrainExists(connection, trainNumber), trainId);
    }

    /**
     * Test for method addTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddTrain() throws Exception {

        LocalTime time = LocalTime.now();

        String trainNumber = "number";
        String trainDepartureTime = time.format(formatter);

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().addTrain(connection, trainNumber, trainDepartureTime);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method addTrain from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddTrain() throws Exception {

        LocalTime time = LocalTime.now();

        String trainNumber = "number";
        String trainDepartureTime = time.format(formatter);

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().addTrain(connection, trainNumber, trainDepartureTime));
    }

    /**
     * Test for method deleteTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteTrain() throws Exception {

        int trainId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().deleteTrain(connection, trainId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteTrain from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteTrain() throws Exception {

        int trainId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().deleteTrain(connection, trainId));
    }

    /**
     * Test for method editTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testEditTrain() throws Exception {

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().editTrain(connection, train);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editTrain from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedEditTrain() throws Exception {

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().editTrain(connection, train));
    }

    /**
     * Test for method getCarriagesTypes from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetCarriagesTypes() throws Exception {
        Map<Integer, String> types = new HashMap<>();
        types.put(1, "Тип");

        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLTrainDAOQuery.GET_CARRIAGES_TYPES)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("type")).thenReturn("Тип");

        assertEquals(DBManager.getInstance().getTrainDAO().getCarriagesTypes(connection), types);
    }

    /**
     * Test for method checkIfTrainHasCarriageWithThisNumber from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfTrainHasCarriageWithThisNumber() throws Exception {
        int trainId = 1;
        int carriageNumber = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_TRAIN_HAS_CARRIAGE_WITH_THIS_NUMBER)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);

        assertEquals(DBManager.getInstance().getTrainDAO().checkIfTrainHasCarriageWithThisNumber(connection, trainId, carriageNumber), 1);
    }

    /**
     * Test for method checkIfCarriageExists from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfCarriageExists() throws Exception {
        int carriageNumber = 1;
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_CARRIAGE_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);

        assertEquals(DBManager.getInstance().getTrainDAO().checkIfCarriageExists(connection, carriageNumber, typeId), 1);
    }

    /**
     * Test for method createCarriage from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCreateCarriage() throws Exception {
        int carriageNumber = 1;
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.CREATE_CARRIAGE, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt(1)).thenReturn(1);

        assertEquals(DBManager.getInstance().getTrainDAO().createCarriage(connection, carriageNumber, typeId), 1);
    }

    /**
     * Test for method createCarriage from {@link MySQLTrainDAO} when station not added to database.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedCreateCarriage() throws Exception {
        int carriageNumber = 1;
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.CREATE_CARRIAGE, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().createCarriage(connection, carriageNumber, typeId));
    }

    /**
     * Test for method createCarriage from {@link MySQLTrainDAO} when not get generated keys.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCreateCarriageNotKeys() throws Exception {
        int carriageNumber = 1;
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.CREATE_CARRIAGE, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().createCarriage(connection, carriageNumber, typeId));
    }

    /**
     * Test for method addCarriageToTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddCarriageToTrain() throws Exception {
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_CARRIAGE_TO_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().addCarriageToTrain(connection, trainId, carriageId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method addCarriageToTrain from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddCarriageToTrain() throws Exception {
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_CARRIAGE_TO_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().addCarriageToTrain(connection, trainId, carriageId));
    }

    /**
     * Test for method getCarriageMaxSeats from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetCarriageMaxSeats() throws Exception {
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_CARRIAGE_MAX_SEATS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("max_seats")).thenReturn(1);

        assertEquals(DBManager.getInstance().getTrainDAO().getCarriageMaxSeats(connection, carriageId), 1);
    }

    /**
     * Test for method getCarriageMaxSeats from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedGetCarriageMaxSeats() throws Exception {
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_CARRIAGE_MAX_SEATS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().getCarriageMaxSeats(connection, carriageId));
    }

    /**
     * Test for method deleteCarriageFromTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteCarriageFromTrain() throws Exception {
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_CARRIAGE_FROM_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().deleteCarriageFromTrain(connection, trainId, carriageId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteCarriageFromTrain from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteCarriageFromTrain() throws Exception {
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_CARRIAGE_FROM_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().deleteCarriageFromTrain(connection, trainId, carriageId));
    }

    /**
     * Test for method checkIfCarriageTypeExists from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfCarriageTypeExists() throws Exception {
        String carriageType = "Тип";

        when(connection.prepareStatement(MySQLTrainDAOQuery.CHECK_IF_CARRIAGE_TYPE_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);

        assertEquals(DBManager.getInstance().getTrainDAO().checkIfCarriageTypeExists(connection, carriageType), 1);
    }

    /**
     * Test for method editCarriageNumberInTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testEditCarriageNumberInTrain() throws Exception {
        int newCarriageId = 2;
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_CARRIAGE_NUMBER_IN_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().editCarriageNumberInTrain(connection, newCarriageId, trainId, carriageId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editCarriageNumberInTrain from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedEditCarriageNumberInTrain() throws Exception {
        int newCarriageId = 2;
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_CARRIAGE_NUMBER_IN_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().editCarriageNumberInTrain(connection, newCarriageId, trainId, carriageId));
    }

    /**
     * Test for method addCarriageType from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddCarriageType() throws Exception {
        String carriageType = "Тип";
        int maxSeats = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_CARRIAGE_TYPE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().addCarriageType(connection, carriageType, maxSeats);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method addCarriageType from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddCarriageType() throws Exception {
        String carriageType = "Тип";
        int maxSeats = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_CARRIAGE_TYPE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().addCarriageType(connection, carriageType, maxSeats));
    }

    /**
     * Test for method checkIfCarriageTypeExists from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void deleteCarriageType() throws Exception {
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_CARRIAGE_TYPE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().deleteCarriageType(connection, typeId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method checkIfCarriageTypeExists from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void deleteFailedCarriageType() throws Exception {
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_CARRIAGE_TYPE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().deleteCarriageType(connection, typeId));
    }

    /**
     * Test for method editCarriageType from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void editCarriageType() throws Exception {
        String newCarriageType = "Новий тип";
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_CARRIAGE_TYPE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().editCarriageType(connection, newCarriageType, typeId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editCarriageType from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void editFailedCarriageType() throws Exception {
        String newCarriageType = "Новий тип";
        int typeId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_CARRIAGE_TYPE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().editCarriageType(connection, newCarriageType, typeId));
    }

    /**
     * Test for method deleteStationFromTrainRoute from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteStationFromTrainRoute() throws Exception {

        int trainId = 1;
        int stationId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_STATION_FROM_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().deleteStationFromTrainRoute(connection, trainId, stationId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteStationFromTrainRoute from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteStationFromTrainRoute() throws Exception {

        int trainId = 1;
        int stationId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_STATION_FROM_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().deleteStationFromTrainRoute(connection, trainId, stationId));
    }

    /**
     * Test for method addStationToTrainRoute from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddStationToTrainRoute() throws Exception {

        LocalTime time = LocalTime.now();

        String timeSinceStart = time.format(formatter);
        String stopTime = time.format(formatter);
        int distanceFromStart = 100;
        int trainId = 1;
        int stationId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_STATION_TO_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().addStationToTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method addStationToTrainRoute from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddStationToTrainRoute() throws Exception {

        LocalTime time = LocalTime.now();

        String timeSinceStart = time.format(formatter);
        String stopTime = time.format(formatter);
        int distanceFromStart = 100;
        int trainId = 1;
        int stationId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_STATION_TO_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().addStationToTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId));
    }

    /**
     * Test for method editStationDataOnTrainRoute from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testEditStationDataOnTrainRoute() throws Exception {

        LocalTime time = LocalTime.now();

        String timeSinceStart = time.format(formatter);
        String stopTime = time.format(formatter);
        int distanceFromStart = 100;
        int trainId = 1;
        int stationId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_STATION_DATA_ON_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getTrainDAO().editStationDataOnTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editStationDataOnTrainRoute from {@link MySQLTrainDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedEditStationDataOnTrainRoute() throws Exception {

        LocalTime time = LocalTime.now();

        String timeSinceStart = time.format(formatter);
        String stopTime = time.format(formatter);
        int distanceFromStart = 100;
        int trainId = 1;
        int stationId = 1;

        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_STATION_DATA_ON_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().editStationDataOnTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId));
    }

    /**
     * Test for method getTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetTrain() throws Exception {

        int trainId = 1;

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getInt("seats")).thenReturn(300);
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getTrain(connection, trainId), train);
    }
}
