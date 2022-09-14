package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainSearchParameterService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Tests for methods from {@link ChooseSeatsPageCommand}
 *
 * @author Yevhen Pashchenko
 */
public class ChooseSeatsPageCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ParameterService<String> searchTrainService = mock(TrainSearchParameterService.class);

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
     * Test for method execute from {@link ChooseSeatsPageCommand}.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();

        String trainId = "1";
        String from = "1";
        String to = "2";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String carriageNumber = "1";
        String carriageType = "П";
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
        carriage.setType(carriageType);
        carriage.addSeat(Integer.parseInt(carriages[0]));
        train.addCarriage(carriage.getId(), carriage);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("departureDate")).thenReturn(date);
        when(request.getParameter("carriageNumber")).thenReturn(carriageNumber);
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(request.getParameterValues("passengerSurname")).thenReturn(passengerSurname);
        when(request.getParameterValues("passengerName")).thenReturn(passengerName);
        when(request.getParameterValues("carriage")).thenReturn(carriages);
        when(request.getParameterValues("seat")).thenReturn(seat);
        when(request.getParameterValues("cost")).thenReturn(cost);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrain(connection, Integer.parseInt(trainId))).thenReturn(train);

        assertEquals("seats.jsp", new ChooseSeatsPageCommand().execute(request, response));
        verify(request, times(1)).setAttribute("carriageType", carriageType);
    }

    /**
     * Test for method execute from {@link ChooseSeatsPageCommand} when user is not in session.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        assertEquals("controller?command=mainPage", new ChooseSeatsPageCommand().execute(request, response));
        verifyNoInteractions(connection);
    }

    /**
     * Test for method execute from {@link ChooseSeatsPageCommand} when incorrect parameters in request.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        User user = new User();

        when((User) session.getAttribute("user")).thenReturn(user);

        ChooseSeatsPageCommand chooseSeatsPageCommand = new ChooseSeatsPageCommand();

        Field field = chooseSeatsPageCommand.getClass().getDeclaredField("searchTrainService");
        field.setAccessible(true);
        field.set(chooseSeatsPageCommand, searchTrainService);

        assertEquals("controller?command=mainPage", chooseSeatsPageCommand.execute(request, response));
        verifyNoInteractions(searchTrainService);
    }
}
