package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.Station;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link MySQLStationDAO}.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLStationDAOTest {

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
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
    }

    /**
     * Test for method getStations from {@link MySQLStationDAO} with locale equals null.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetStations() throws Exception {

        Station station = new Station();
        station.setId(1);
        station.setName("Станція");

        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLStationDAOQuery.GET_STATIONS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Станція");

        assertEquals(Collections.singletonList(station), DBManager.getInstance().getStationDAO().getStations(connection, null));
    }

    /**
     * Test for method getStations from {@link MySQLStationDAO} with locale equals uk.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetStationsLocaleUA() throws Exception {

        Station station = new Station();
        station.setId(1);
        station.setName("Станція");

        String locale = "uk";

        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLStationDAOQuery.GET_STATIONS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Станція");

        assertEquals(Collections.singletonList(station), DBManager.getInstance().getStationDAO().getStations(connection, locale));
    }

    /**
     * Test for method getStations from {@link MySQLStationDAO} with locale equals en.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetStationsLocaleEN() throws Exception {

        Station station = new Station();
        station.setId(1);
        station.setName("Station");

        String locale = "en";

        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLStationDAOQuery.GET_EN_STATIONS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Station");

        assertEquals(Collections.singletonList(station), DBManager.getInstance().getStationDAO().getStations(connection, locale));
    }

    /**
     * Test for method checkIfStationExists from {@link MySQLStationDAO} with locale equals null.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfStationExists() throws Exception {
        String stationName = "Станція";

        when(connection.prepareStatement(MySQLStationDAOQuery.CHECK_IF_STATION_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);

        assertEquals(DBManager.getInstance().getStationDAO().checkIfStationExists(connection, stationName, null), 1);
    }

    /**
     * Test for method checkIfStationExists from {@link MySQLStationDAO} with locale equals uk.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfStationExistsLocaleUA() throws Exception {
        String stationName = "Станція";

        when(connection.prepareStatement(MySQLStationDAOQuery.CHECK_IF_STATION_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);

        assertEquals(DBManager.getInstance().getStationDAO().checkIfStationExists(connection, stationName, "uk"), 1);
    }

    /**
     * Test for method checkIfStationExists from {@link MySQLStationDAO} with locale equals en.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfStationExistsLocaleEN() throws Exception {
        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.CHECK_IF_STATION_EN_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);

        assertEquals(DBManager.getInstance().getStationDAO().checkIfStationExists(connection, stationName, "en"), 1);
    }

    /**
     * Test for method addStation from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddStation() throws Exception {

        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt(1)).thenReturn(1);

        assertEquals(1, DBManager.getInstance().getStationDAO().addStation(connection, stationName));
    }

    /**
     * Test for method addStation from {@link MySQLStationDAO} when station not added to database.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddStation() throws Exception {

        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().addStation(connection, stationName));
    }

    /**
     * Test for method addStation from {@link MySQLStationDAO} when not get generated keys.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddStationNotKeys() throws Exception {

        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().addStation(connection, stationName));
    }

    /**
     * Test for method addStationEN from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddStationEN() throws Exception {

        int stationId = 1;
        String stationNameEN = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION_EN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getStationDAO().addStationEN(connection, stationId, stationNameEN);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method addStationEN from {@link MySQLStationDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddStationEN() throws Exception {

        int stationId = 1;
        String stationNameEN = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION_EN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().addStationEN(connection, stationId, stationNameEN));
    }

    /**
     * Test for method deleteStation from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteStation() throws Exception {

        int stationId = 1;

        when(connection.prepareStatement(MySQLStationDAOQuery.DELETE_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getStationDAO().deleteStation(connection, stationId);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteStation from {@link MySQLStationDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteStation() throws Exception {

        int stationId = 1;

        when(connection.prepareStatement(MySQLStationDAOQuery.DELETE_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().deleteStation(connection, stationId));
    }

    /**
     * Test for method editStation from {@link MySQLStationDAO}  with locale equals null.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testEditStation() throws Exception {

        int stationId = 1;
        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.EDIT_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getStationDAO().editStation(connection, stationId, stationName, null);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editStation from {@link MySQLStationDAO}  with locale equals uk.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testEditStationLocaleUA() throws Exception {

        int stationId = 1;
        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.EDIT_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getStationDAO().editStation(connection, stationId, stationName, "uk");
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editStation from {@link MySQLStationDAO}  with locale equals en.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testEditStationLocaleEN() throws Exception {

        int stationId = 1;
        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.EDIT_EN_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getStationDAO().editStation(connection, stationId, stationName, "en");
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method editStation from {@link MySQLStationDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedEditStation() throws Exception {

        int stationId = 1;
        String stationName = "Station";

        when(connection.prepareStatement(MySQLStationDAOQuery.EDIT_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().editStation(connection, stationId, stationName, null));
    }
}
