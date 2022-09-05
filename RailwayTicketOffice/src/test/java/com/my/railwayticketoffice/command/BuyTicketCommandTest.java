package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.Mail;
import com.my.railwayticketoffice.service.TicketService;
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
import java.util.*;

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
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final TicketService ticketService = mock(TicketService.class);
    private final Mail mail = mock(Mail.class);

    /**
     * Test for method execute from {@link BuyTicketCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        List<String> dateForDB = Arrays.asList(date.split("\\."));
        Collections.reverse(dateForDB);

        Train train = new Train();
        train.setSeats(1);

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn("1");
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameterValues("passengerSurname")).thenReturn(new String[] {"passengerSurname"});
        when(request.getParameterValues("passengerName")).thenReturn(new String[] {"passengerName"});
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrainSpecifiedByDate(connection, 1, String.join("-", dateForDB))).thenReturn(train);
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(scheduleDAO.getTrainAvailableSeatsOnThisDate(connection, 1, String.join("-", dateForDB))).thenReturn(1);

        Command buyTicketCommand = new BuyTicketCommand();
        Field field = buyTicketCommand.getClass().getDeclaredField("ticketService");
        field.setAccessible(true);
        field.set(buyTicketCommand, ticketService);
        field = buyTicketCommand.getClass().getDeclaredField("mail");
        field.setAccessible(true);
        field.set(buyTicketCommand, mail);


        assertEquals("success.jsp", buyTicketCommand.execute(request, response));
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
        String date = dateNow.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        when(request.getParameter("departureDate")).thenReturn(date);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);

        assertEquals("controller?command=getTrains&from=1&to=2&departureDate=" + date, new BuyTicketCommand().execute(request, response));
        DBManagerMocked.close();
    }

    /**
     * Test for method execute from {@link BuyTicketCommand} when user is not in session and no additional parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUserInSessionAndNoAdditionalParameters() throws Exception {

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(null);
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);

        assertEquals("controller?command=mainPage", new BuyTicketCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
