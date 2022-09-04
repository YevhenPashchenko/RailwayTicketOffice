package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link MySQLScheduleDAO}.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLScheduleDAOTest {

    private final ResultSet rs = mock(ResultSet.class);
    private final PreparedStatement pstmt = mock(PreparedStatement.class);
    private final Connection connection = mock(Connection.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);

    /**
     * Test for method addData from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddData() throws Exception {

        LocalDate date = LocalDate.now();
        List<String> scheduleDates = new ArrayList<>();
        scheduleDates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)));

        Train train = new Train();
        train.setId(1);
        train.setSeats(300);

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.ADD_DATA + MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().addData(connection, scheduleDates, Collections.singletonList(train)));
        DBManagerMocked.close();

    }

    /**
     * Test for method deleteData from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testDeleteData() throws Exception {

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_DATA)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().deleteData(connection, currentDate));
        DBManagerMocked.close();
    }

    /**
     * Test for method getTrainAvailableSeatsOnThisDate from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.GET_AVAILABLE_SEATS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("available_seats")).thenReturn(1);

        assertEquals(DBManager.getInstance().getScheduleDAO().getTrainAvailableSeatsOnThisDate(connection, trainId, currentDate), 1);
        DBManagerMocked.close();
    }

    /**
     * Test for method addData from {@link MySQLScheduleDAO} when failed to get train available seats.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testFailedGetTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.GET_AVAILABLE_SEATS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().getTrainAvailableSeatsOnThisDate(connection, trainId, currentDate));
        DBManagerMocked.close();

    }

    /**
     * Test for method changeTrainAvailableSeatsOnThisDate from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testChangeTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
        int availableSeatsNow = 300;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHANGE_AVAILABLE_SEATS)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().changeTrainAvailableSeatsOnThisDate(connection, trainId, currentDate, availableSeatsNow));
        DBManagerMocked.close();
    }

    /**
     * Test for method checkIfRecordExists from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testCheckIfRecordExists() throws Exception {

        int trainId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_RECORD_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("exist")).thenReturn(1);

        assertTrue(DBManager.getInstance().getScheduleDAO().checkIfRecordExists(connection, trainId));
        DBManagerMocked.close();
    }

    /**
     * Test for method checkIfRecordExists from {@link MySQLScheduleDAO} when failed to check if record exist.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testFailedCheckIfRecordExists() throws Exception {

        int trainId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_RECORD_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().checkIfRecordExists(connection, trainId));
        DBManagerMocked.close();
    }

    /**
     * Test for method deleteTrainFromSchedule from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testDeleteTrainFromSchedule() throws Exception {

        int trainId = 1;

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_TRAIN_FROM_SCHEDULE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().deleteTrainFromSchedule(connection, trainId));
        DBManagerMocked.close();
    }
}
