package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link TicketPageCommand}
 *
 * @author Yevhen Pashchenko
 */
public class TicketPageCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
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
     * Test for method execute from {@link TicketPageCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String carriageType = "Тип";
        String[] carriage = new String[] {"1"};
        String[] seat = new String[] {"1"};
        String[] cost = new String[] {"1"};

        Train train = new Train();

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(request.getParameterValues("carriage")).thenReturn(carriage);
        when(request.getParameterValues("seat")).thenReturn(seat);
        when(request.getParameterValues("cost")).thenReturn(cost);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrain(connection, Integer.parseInt(trainId))).thenReturn(train);

        assertEquals("ticket.jsp", new TicketPageCommand().execute(request, response));
        verify(trainDAO, times(1)).getRoutesForTrains(connection, Collections.singletonList(train), null);
    }

    /**
     * Test for method execute from {@link TicketPageCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteUserNotInSession() throws Exception {

        TicketPageCommand ticketPageCommand = new TicketPageCommand();

        Field field = ticketPageCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(ticketPageCommand, trainService);

        assertEquals("controller?command=mainPage", ticketPageCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link TicketPageCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new TicketPageCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
