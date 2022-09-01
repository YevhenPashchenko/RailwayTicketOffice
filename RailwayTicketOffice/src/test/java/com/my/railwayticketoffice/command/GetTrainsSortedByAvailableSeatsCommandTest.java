package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link GetTrainsSortedByAvailableSeatsCommand}
 *
 * @author Yevhen Pashchenko
 */
public class GetTrainsSortedByAvailableSeatsCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);

    /**
     * Test for method execute from {@link GetTrainsSortedByAvailableSeatsCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        LocalDate date = LocalDate.now();
        List<Train> trains = new ArrayList<>();
        Train train = new Train();
        train.setDepartureTime(LocalTime.now());
        train.getRoute().addDistanceFromStart(1, 1);
        train.getRoute().addDistanceFromStart(2, 2);
        train.getRoute().addTimeSinceStart(1, "00:01");
        train.getRoute().addTimeSinceStart(2, "00:02");
        trains.add(train);

        when(request.getSession()).thenReturn(session);
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("from")).thenReturn("1");
        when(request.getParameter("to")).thenReturn("2");
        when(request.getParameter("datePicker")).thenReturn(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getTrainsSpecifiedByStationsAndDate(connection, 1, 2, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))).thenReturn(trains);

        assertEquals("controller?command=mainPage", new GetTrainsSortedByAvailableSeatsCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
