package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link AddTrainToScheduleCommand}
 *
 * @author Yevhen Pashchenko
 */
public class AddTrainToScheduleCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);

    /**
     * Test for method execute from {@link AddTrainToScheduleCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        Train train = new Train();
        train.setDepartureTime(LocalTime.now());

        when(request.getSession()).thenReturn(session);
        when((User) session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("trainId")).thenReturn("1");
        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(scheduleDAO.checkIfRecordExists(connection, 1)).thenReturn(false);
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);

        assertEquals("controller?command=mainPage", new AddTrainToScheduleCommand().execute(request, response));
        DBManagerMocked.close();
    }
}
