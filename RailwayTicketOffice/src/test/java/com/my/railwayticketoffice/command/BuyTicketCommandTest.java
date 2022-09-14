package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.Mail;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TicketParameterService;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link BuyTicketCommand}
 *
 * @author Yevhen Pashchenko
 */
public class BuyTicketCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ParameterService<String> trainService = mock(TrainParameterService.class);
    private final ParameterService<String[]> ticketParameterService = mock(TicketParameterService.class);
    private final Mail mail = mock(Mail.class);

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
     * Test for method execute from {@link BuyTicketCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();

        String trainId = "1";
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String[] passengerSurname = new String[] {"passengerSurname"};
        String[] passengerName = new String[] {"passengerName"};
        String[] carriages = new String[] {"1"};
        String[] seat = new String[] {"1"};
        String[] cost = new String[] {"1"};

        List<String> dateForDB = Arrays.asList(date.split("\\."));
        Collections.reverse(dateForDB);

        Train train = new Train();
        train.setDepartureTime(LocalTime.of(0, 0));
        train.getRoute().addDistanceFromStart(1, 1);
        train.getRoute().addDistanceFromStart(2, 2);
        train.getRoute().addTimeSinceStart(1, "00:01");
        train.getRoute().addTimeSinceStart(2, "00:02");
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(1);
        carriage.setNumber(1);
        carriage.setMaxSeats(1);
        carriage.setType("П");
        carriage.addSeat(Integer.parseInt(carriages[0]));
        train.addCarriage(carriage.getId(), carriage);

        when((User) session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameterValues("passengerSurname")).thenReturn(passengerSurname);
        when(request.getParameterValues("passengerName")).thenReturn(passengerName);
        when(request.getParameterValues("carriage")).thenReturn(carriages);
        when(request.getParameterValues("seat")).thenReturn(seat);
        when(request.getParameterValues("cost")).thenReturn(cost);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);

        BuyTicketCommand buyTicketCommand = new BuyTicketCommand();

        Field field = buyTicketCommand.getClass().getDeclaredField("mail");
        field.setAccessible(true);
        field.set(buyTicketCommand, mail);

        assertEquals("success.jsp", buyTicketCommand.execute(request, response));
        verify(mail, times(1)).send(user, session);
    }

    /**
     * Test for method execute from {@link BuyTicketCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {
        String from = "1";
        String to = "2";
        LocalDate dateNow = LocalDate.now();
        String date = dateNow.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);

        BuyTicketCommand buyTicketCommand = new BuyTicketCommand();

        Field field = buyTicketCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(buyTicketCommand, trainService);

        assertEquals("controller?command=getTrains&from=" + from + "&to=" + to + "&departureDate=" + date, buyTicketCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link BuyTicketCommand} when incorrect parameters in request.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        User user = new User();

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new BuyTicketCommand().execute(request, response));
        verifyNoInteractions(ticketParameterService);
    }
}
