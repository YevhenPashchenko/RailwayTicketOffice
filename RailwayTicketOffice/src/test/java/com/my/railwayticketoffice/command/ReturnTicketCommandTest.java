package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.ReturnTicketParameterService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link ReturnTicketCommand}
 *
 * @author Yevhen Pashchenko
 */
public class ReturnTicketCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final ParameterService<String> returnTicketService = mock(ReturnTicketParameterService.class);

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
     * Test for method execute from {@link ReturnTicketCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setId(1);

        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String ticketNumber = "1-1-1-111111111111-111111111111";

        String[] ticketData = ticketNumber.split("-");
        int trainId = Integer.parseInt(ticketData[0]);
        int carriageId = 1;
        int carriageNumber = Integer.parseInt(ticketData[1]);
        int seatNumber = Integer.parseInt(ticketData[2]);
        String departureDate = ticketData[3].substring(4, 8) + "-" + ticketData[3].substring(2, 4) + "-" + ticketData[3].substring(0, 2);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("ticketNumber")).thenReturn(ticketNumber);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, trainId, carriageNumber)).thenReturn(carriageId);
        when(scheduleDAO.checkIfSeatBooked(connection, departureDate, trainId, carriageId, seatNumber)).thenReturn(user.getId());

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, new ReturnTicketCommand().execute(request, response));
        verify(scheduleDAO, times(1)).returnTicket(connection, departureDate, trainId, carriageId, seatNumber);
    }

    /**
     * Test for method execute from {@link ReturnTicketCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);

        ReturnTicketCommand returnTicketCommand = new ReturnTicketCommand();

        Field field = returnTicketCommand.getClass().getDeclaredField("returnTicketService");
        field.setAccessible(true);
        field.set(returnTicketCommand, returnTicketService);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, returnTicketCommand.execute(request, response));
        verifyNoInteractions(returnTicketService);
    }

    /**
     * Test for method execute from {@link ReturnTicketCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new ReturnTicketCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
