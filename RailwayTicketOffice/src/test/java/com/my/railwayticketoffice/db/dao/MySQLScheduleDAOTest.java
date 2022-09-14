package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link MySQLScheduleDAO}.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLScheduleDAOTest {

    private final ResultSet rs = mock(ResultSet.class);
    private final PreparedStatement pstmt = mock(PreparedStatement.class);
    private final Connection connection = mock(Connection.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);

    @BeforeEach
    void beforeEach() throws Exception {
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManagerInstance.getScheduleDAO()).thenReturn(new MySQLScheduleDAO());
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
    }

    /**
     * Test for method addData from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testAddData() throws Exception {

        LocalDate date = LocalDate.now();
        List<String> scheduleDates = new ArrayList<>();
        scheduleDates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)));

        Train train = new Train();
        train.setId(1);

        when(connection.prepareStatement(MySQLScheduleDAOQuery.ADD_DATA + MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getScheduleDAO().addData(connection, scheduleDates, Collections.singletonList(train));

        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method addData from {@link MySQLScheduleDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedAddData() throws Exception {

        LocalDate date = LocalDate.now();
        List<String> scheduleDates = new ArrayList<>();
        scheduleDates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)));

        Train train = new Train();
        train.setId(1);

        when(connection.prepareStatement(MySQLScheduleDAOQuery.ADD_DATA + MySQLScheduleDAOQuery.VALUES_FOR_ADD_DATA)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().addData(connection, scheduleDates, Collections.singletonList(train)));
    }

    /**
     * Test for method deleteData from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteData() throws Exception {

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_DATA)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getScheduleDAO().deleteData(connection, currentDate);

        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteData from {@link MySQLScheduleDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteData() throws Exception {

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_DATA)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().deleteData(connection, currentDate));
    }

    /**
     * Test for method deleteCarriageFromSchedule from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteCarriageFromSchedule() throws Exception {
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_CARRIAGE_FROM_SCHEDULE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getScheduleDAO().deleteCarriageFromSchedule(connection, trainId, carriageId);

        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteCarriageFromSchedule from {@link MySQLScheduleDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteCarriageFromSchedule() throws Exception {
        int trainId = 1;
        int carriageId = 1;

        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_CARRIAGE_FROM_SCHEDULE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().deleteCarriageFromSchedule(connection, trainId, carriageId));
    }

    /**
     * Test for method getTrainAvailableSeatsOnThisDate from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testGetTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        when(connection.prepareStatement(MySQLScheduleDAOQuery.GET_AVAILABLE_SEATS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("available_seats")).thenReturn(1);

        assertEquals(DBManager.getInstance().getScheduleDAO().getTrainAvailableSeatsOnThisDate(connection, trainId, currentDate), 1);
    }

    /**
     * Test for method addData from {@link MySQLScheduleDAO} when failed to get train available seats.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedGetTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));

        when(connection.prepareStatement(MySQLScheduleDAOQuery.GET_AVAILABLE_SEATS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().getTrainAvailableSeatsOnThisDate(connection, trainId, currentDate));
    }

    /**
     * Test for method checkIfRecordExists from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testCheckIfRecordExists() throws Exception {

        int trainId = 1;

        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_RECORD_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("exist")).thenReturn(1);

        assertTrue(DBManager.getInstance().getScheduleDAO().checkIfRecordExists(connection, trainId));
    }

    /**
     * Test for method checkIfRecordExists from {@link MySQLScheduleDAO} when failed to check if record exist.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedCheckIfRecordExists() throws Exception {

        int trainId = 1;

        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHECK_IF_RECORD_EXISTS)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().checkIfRecordExists(connection, trainId));
    }

    /**
     * Test for method deleteTrainFromSchedule from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testDeleteTrainFromSchedule() throws Exception {

        int trainId = 1;

        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_TRAIN_FROM_SCHEDULE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getScheduleDAO().deleteTrainFromSchedule(connection, trainId);

        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method deleteTrainFromSchedule from {@link MySQLScheduleDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedDeleteTrainFromSchedule() throws Exception {

        int trainId = 1;

        when(connection.prepareStatement(MySQLScheduleDAOQuery.DELETE_TRAIN_FROM_SCHEDULE)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().deleteTrainFromSchedule(connection, trainId));
    }

    /**
     * Test for method changeTrainAvailableSeatsOnThisDate from {@link MySQLScheduleDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testChangeTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
        int carriageId = 1;
        List<Integer> seatsNumbers = new ArrayList<>();
        seatsNumbers.add(1);

        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHANGE_AVAILABLE_SEATS + "(?)")).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(1);

        DBManager.getInstance().getScheduleDAO().changeTrainAvailableSeatsOnThisDate(connection, trainId, currentDate, carriageId, seatsNumbers);
        verify(pstmt, times(1)).executeUpdate();
    }

    /**
     * Test for method changeTrainAvailableSeatsOnThisDate from {@link MySQLScheduleDAO} failed.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testFailedChangeTrainAvailableSeatsOnThisDate() throws Exception {

        int trainId = 1;
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
        int carriageId = 1;
        List<Integer> seatsNumbers = new ArrayList<>();
        seatsNumbers.add(1);

        when(connection.prepareStatement(MySQLScheduleDAOQuery.CHANGE_AVAILABLE_SEATS + "(?)")).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getScheduleDAO().changeTrainAvailableSeatsOnThisDate(connection, trainId, currentDate, carriageId, seatsNumbers));
    }
}
