package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private final DBManager DBManagerInstance = mock(DBManager.class);

    /**
     * Test for method getAllTrains from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetAllTrains() throws Exception {

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setSeats(300);
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLTrainDAOQuery.GET_ALL_TRAINS_DATA)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getInt("seats")).thenReturn(300);
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getAllTrains(connection), Collections.singletonList(train));
        DBManagerMocked.close();
    }

    /**
     * Test for method getTrainsSpecifiedByStationsAndDate from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetTrainsSpecifiedByStationsAndDate() throws Exception {

        LocalTime time = LocalTime.now();

        int fromStationId = 1;
        int toStationId = 2;
        String formattedDate = time.format(formatter);

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setSeats(300);
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAINS_SPECIFIED_BY_STATIONS_AND_DATE)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getInt("available_seats")).thenReturn(300);
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getTrainsSpecifiedByStationsAndDate(connection, fromStationId, toStationId, formattedDate), Collections.singletonList(train));
        DBManagerMocked.close();
    }

    /**
     * Test for method getRoutesForTrains from {@link MySQLTrainDAO} with locale equals null.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetRoutesForTrains() throws Exception {

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

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
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
        DBManagerMocked.close();
    }

    /**
     * Test for method getRoutesForTrains from {@link MySQLTrainDAO} with locale equals uk.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetRoutesForTrainsLocaleUA() throws Exception {

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

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
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
        DBManagerMocked.close();
    }

    /**
     * Test for method getRoutesForTrains from {@link MySQLTrainDAO} with locale equals en.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetRoutesForTrainsLocaleEN() throws Exception {

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

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
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
        DBManagerMocked.close();
    }

    private Train getTrain(LocalTime time) {
        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setSeats(300);
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));
        return train;
    }

    /**
     * Test for method getTrainThatIsInSchedule from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetTrainThatIsInSchedule() throws Exception {

        int trainId = 1;

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setSeats(300);
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAIN_THAT_IS_IN_SCHEDULE)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getInt("available_seats")).thenReturn(300);
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getTrainThatIsInSchedule(connection, trainId), train);
        DBManagerMocked.close();
    }

    /**
     * Test for method addTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddTrain() throws Exception {

        LocalTime time = LocalTime.now();

        String trainNumber = "number";
        int trainSeats = 300;
        String trainDepartureTime = time.format(formatter);

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().addTrain(connection, trainNumber, trainSeats, trainDepartureTime));
        DBManagerMocked.close();
    }

    /**
     * Test for method deleteTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testDeleteTrain() throws Exception {

        int trainId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().deleteTrain(connection, trainId));
        DBManagerMocked.close();
    }

    /**
     * Test for method editTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testEditTrain() throws Exception {

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setSeats(300);
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().editTrain(connection, train));
        DBManagerMocked.close();
    }

    /**
     * Test for method deleteStationFromTrainRoute from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testDeleteStationFromTrainRoute() throws Exception {

        int trainId = 1;
        int stationId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.DELETE_STATION_FROM_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().deleteStationFromTrainRoute(connection, trainId, stationId));
        DBManagerMocked.close();
    }

    /**
     * Test for method addStationToTrainRoute from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddStationToTrainRoute() throws Exception {

        LocalTime time = LocalTime.now();

        String timeSinceStart = time.format(formatter);
        String stopTime = time.format(formatter);
        int distanceFromStart = 100;
        int trainId = 1;
        int stationId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.ADD_STATION_TO_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().addStationToTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId));
        DBManagerMocked.close();
    }

    /**
     * Test for method editStationDataOnTrainRoute from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testEditStationDataOnTrainRoute() throws Exception {

        LocalTime time = LocalTime.now();

        String timeSinceStart = time.format(formatter);
        String stopTime = time.format(formatter);
        int distanceFromStart = 100;
        int trainId = 1;
        int stationId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.EDIT_STATION_DATA_ON_TRAIN_ROUTE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getTrainDAO().editStationDataOnTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId));
        DBManagerMocked.close();
    }

    /**
     * Test for method getTrain from {@link MySQLTrainDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetTrain() throws Exception {

        int trainId = 1;

        LocalTime time = LocalTime.now();

        Train train = new Train();
        train.setId(1);
        train.setNumber("number");
        train.setSeats(300);
        train.setDepartureTime(LocalTime.parse(time.format(formatter)));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getTrainDAO()).thenReturn(new MySQLTrainDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLTrainDAOQuery.GET_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("number")).thenReturn("number");
        when(rs.getInt("seats")).thenReturn(300);
        when(rs.getString("departure_time")).thenReturn(time.format(formatter));

        assertEquals(DBManager.getInstance().getTrainDAO().getTrain(connection, trainId), train);
        DBManagerMocked.close();
    }
}
