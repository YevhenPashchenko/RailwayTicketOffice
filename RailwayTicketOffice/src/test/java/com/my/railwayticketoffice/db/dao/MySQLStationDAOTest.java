package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.Station;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private final DBManager DBManagerInstance = mock(DBManager.class);

    /**
     * Test for method getStations from {@link MySQLStationDAO} with locale equals null.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetStations() throws Exception {

        Station station = new Station();
        station.setId(1);
        station.setName("Станція");

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLStationDAOQuery.GET_STATIONS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Станція");

        assertEquals(Collections.singletonList(station), DBManager.getInstance().getStationDAO().getStations(connection, null));
        DBManagerMocked.close();
    }

    /**
     * Test for method getStations from {@link MySQLStationDAO} with locale equals uk.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetStationsLocaleUA() throws Exception {

        Station station = new Station();
        station.setId(1);
        station.setName("Станція");

        String locale = "uk";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLStationDAOQuery.GET_STATIONS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Станція");

        assertEquals(Collections.singletonList(station), DBManager.getInstance().getStationDAO().getStations(connection, locale));
        DBManagerMocked.close();
    }

    /**
     * Test for method getStations from {@link MySQLStationDAO} with locale equals en.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetStationsLocaleEN() throws Exception {

        Station station = new Station();
        station.setId(1);
        station.setName("Station");

        String locale = "en";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(MySQLStationDAOQuery.GET_EN_STATIONS)).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("name")).thenReturn("Station");

        assertEquals(Collections.singletonList(station), DBManager.getInstance().getStationDAO().getStations(connection, locale));
        DBManagerMocked.close();
    }

    /**
     * Test for method addStation from {@link MySQLStationDAO} when station not added to database.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddStationNotAdded() throws Exception {

        String stationName = "Station";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().addStation(connection, stationName));
        DBManagerMocked.close();
    }

    /**
     * Test for method addStation from {@link MySQLStationDAO} when not get generated keys.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddStationNotKeys() throws Exception {

        String stationName = "Station";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().addStation(connection, stationName));
        DBManagerMocked.close();
    }

    /**
     * Test for method addStation from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddStation() throws Exception {

        String stationName = "Station";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION, PreparedStatement.RETURN_GENERATED_KEYS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);
        when(pstmt.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt(1)).thenReturn(1);

        assertEquals(1, DBManager.getInstance().getStationDAO().addStation(connection, stationName));
        DBManagerMocked.close();
    }

    /**
     * Test for method addStationEN from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddStationEN() throws Exception {

        int stationId = 1;
        String stationNameEN = "Station";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLStationDAOQuery.ADD_STATION_EN)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().addStationEN(connection, stationId, stationNameEN));
        DBManagerMocked.close();
    }

    /**
     * Test for method deleteStation from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testDeleteStation() throws Exception {

        int stationId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLStationDAOQuery.DELETE_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().deleteStation(connection, stationId));
        DBManagerMocked.close();
    }

    /**
     * Test for method editStation from {@link MySQLStationDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testEditStation() throws Exception {

        int stationId = 1;
        String stationName = "Station";

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getStationDAO()).thenReturn(new MySQLStationDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLStationDAOQuery.EDIT_STATION)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getStationDAO().editStation(connection, stationId, stationName, null));
        DBManagerMocked.close();
    }
}
