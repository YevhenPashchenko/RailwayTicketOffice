package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link BuyTicketCommand}
 *
 * @author Yevhen Pashchenko
 */
public class BuyTicketCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);

    /**
     * Test for method execute from {@link BuyTicketCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, String[]> passengersDataMap = new HashMap<>();
        passengersDataMap.put("passengerName", new String[] {"passengerName"});
        passengersDataMap.put("passengerSurname", new String[] {"passengerSurname"});

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn("1");
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameterMap()).thenReturn(passengersDataMap);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(scheduleDAO.getTrainAvailableSeatsOnThisDate(connection, 1, date)).thenReturn(1);

        assertEquals("success.jsp", new BuyTicketCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link BuyTicketCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSession() throws Exception {

        LocalDate dateNow = LocalDate.now();
        String date = dateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String departureDate = dateNow.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("fromStationId")).thenReturn("1");
        when(request.getParameter("toStationId")).thenReturn("1");
        when(request.getParameter("departureDate")).thenReturn(date);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);

        assertEquals("controller?command=getTrains&from=1&to=1&datePicker=" + departureDate, new BuyTicketCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
