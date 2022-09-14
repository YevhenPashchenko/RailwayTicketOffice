package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.filter.TrainFilter;
import com.my.railwayticketoffice.filter.TrainFilterByDirectionAndDepartureTime;
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
 * Tests for methods from {@link GetTrainsCommand}
 *
 * @author Yevhen Pashchenko
 */
public class GetTrainsCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final TrainFilter trainFilter = mock(TrainFilterByDirectionAndDepartureTime.class);

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
     * Test for method execute from {@link GetTrainsCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        String page = "1";
        String from = "1";
        String to = "2";
        LocalDate date = LocalDate.now();
        List<String> dateForDB = Arrays.asList(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).split("\\."));
        Collections.reverse(dateForDB);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("page", page);
        parameters.put("from", from);
        parameters.put("to", to);
        parameters.put("date", date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        List<Train> trains = new ArrayList<>();
        Train train = new Train();
        train.setDepartureTime(LocalTime.now());
        train.getRoute().addDistanceFromStart(1, 1);
        train.getRoute().addDistanceFromStart(2, 2);
        train.getRoute().addTimeSinceStart(1, "00:01");
        train.getRoute().addTimeSinceStart(2, "00:02");
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(1);
        carriage.setNumber(1);
        carriage.setMaxSeats(1);
        carriage.setType("Тип");
        train.addCarriage(carriage.getId(), carriage);
        trains.add(train);

        when(request.getParameter("page")).thenReturn(page);
        when(request.getParameter("from")).thenReturn(from);
        when(request.getParameter("to")).thenReturn(to);
        when(request.getParameter("departureDate")).thenReturn(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrainsSpecifiedByStationsAndDate(connection, 1, 2, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).thenReturn(trains);
        when(trainFilter.filter(trains, parameters)).thenReturn(trains);

        GetTrainsCommand getTrainsCommand = new GetTrainsCommand();

        Field field = getTrainsCommand.getClass().getDeclaredField("trainFilter");
        field.setAccessible(true);
        field.set(getTrainsCommand, trainFilter);

        assertEquals("controller?command=mainPage", getTrainsCommand.execute(request, response));
        verify(trainDAO, times(1)).getFreeSeatsForTrainsByDate(connection, trains, String.join("-", dateForDB));
    }

    /**
     * Test for method execute from {@link GetTrainsCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {

        new GetTrainsCommand().execute(request, response);

        assertEquals("controller?command=mainPage", new GetTrainsCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
