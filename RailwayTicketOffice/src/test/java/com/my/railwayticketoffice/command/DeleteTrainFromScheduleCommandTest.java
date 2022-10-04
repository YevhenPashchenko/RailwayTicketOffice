package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link DeleteTrainFromScheduleCommand}
 *
 * @author Yevhen Pashchenko
 */
public class DeleteTrainFromScheduleCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final UserDAO userDAO = mock(UserDAO.class);
    private final ParameterService<String> trainService = mock(TrainParameterService.class);

    @BeforeEach
    void beforeEach() throws Exception {
        when(request.getSession()).thenReturn(session);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link DeleteTrainFromScheduleCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String trainNumber = "Номер";
        String departureDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        List<String> dateForDB = Arrays.asList(departureDate.split("\\."));
        Collections.reverse(dateForDB);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("departureDate")).thenReturn(departureDate);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(trainId))).thenReturn(true);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(Integer.parseInt(trainId));
        when(scheduleDAO.checkIfTrainHasBookedSeatsAtDate(connection, String.join("-", dateForDB), Integer.parseInt(trainId))).thenReturn(true);

        assertEquals("controller?command=mainPage", new DeleteTrainFromScheduleCommand().execute(request, response));
        verify(userDAO, times(1)).getUsersThatPurchasedSeatOnTrainAtDate(connection, String.join("-", dateForDB), Integer.parseInt(trainId));
        verify(scheduleDAO, times(1)).deleteTrainFromScheduleAtDate(connection, String.join("-", dateForDB), Integer.parseInt(trainId));
    }

    /**
     * Test for method execute from {@link DeleteTrainFromScheduleCommand} when users have not purchased tickets on this train at this day.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteUsersNotPurchasedTickets() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String trainNumber = "Номер";
        String departureDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        List<String> dateForDB = Arrays.asList(departureDate.split("\\."));
        Collections.reverse(dateForDB);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("departureDate")).thenReturn(departureDate);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getUserDAO()).thenReturn(userDAO);
        when(scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(trainId))).thenReturn(true);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(Integer.parseInt(trainId));
        when(scheduleDAO.checkIfTrainHasBookedSeatsAtDate(connection, String.join("-", dateForDB), Integer.parseInt(trainId))).thenReturn(false);

        assertEquals("controller?command=mainPage", new DeleteTrainFromScheduleCommand().execute(request, response));
        verify(userDAO, times(0)).getUsersThatPurchasedSeatOnTrainAtDate(connection, String.join("-", dateForDB), Integer.parseInt(trainId));
        verify(scheduleDAO, times(1)).deleteTrainFromScheduleAtDate(connection, String.join("-", dateForDB), Integer.parseInt(trainId));
    }

    /**
     * Test for method execute from {@link DeleteTrainFromScheduleCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        DeleteTrainFromScheduleCommand deleteTrainFromScheduleCommand = new DeleteTrainFromScheduleCommand();

        Field field = deleteTrainFromScheduleCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(deleteTrainFromScheduleCommand, trainService);

        assertEquals("controller?command=mainPage", deleteTrainFromScheduleCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link DeleteTrainFromScheduleCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new DeleteTrainFromScheduleCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
