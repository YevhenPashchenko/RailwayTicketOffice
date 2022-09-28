package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.ScheduleService;
import com.my.railwayticketoffice.service.TrainParameterService;
import com.my.railwayticketoffice.service.TrainScheduleService;
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
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link AddTrainToScheduleCommand}
 *
 * @author Yevhen Pashchenko
 */
public class AddTrainToScheduleCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ParameterService<String> trainService = mock(TrainParameterService.class);
    private final ScheduleService service = new TrainScheduleService();

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
     * Test for method execute from {@link AddTrainToScheduleCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId= "1";
        String trainNumber = "Номер";

        Train train = new Train();
        train.setDepartureTime(LocalTime.now());
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(1);
        train.addCarriage(carriage.getId(), carriage);

        List<String> scheduleDates = service.create();

        when((User) session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(trainId))).thenReturn(false);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(Integer.parseInt(trainId));
        when(trainDAO.getTrain(connection, 1)).thenReturn(train);

        assertEquals("controller?command=mainPage", new AddTrainToScheduleCommand().execute(request, response));
        verify(scheduleDAO, times(1)).addData(connection, scheduleDates, Collections.singletonList(train), null);
    }

    /**
     * Test for method execute from {@link AddTrainToScheduleCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteNotUser() throws Exception {

        AddTrainToScheduleCommand addTrainToScheduleCommand = new AddTrainToScheduleCommand();

        Field field = addTrainToScheduleCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(addTrainToScheduleCommand, trainService);

        assertEquals("controller?command=mainPage", addTrainToScheduleCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link AddTrainToScheduleCommand} when incorrect parameters in request.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteIncorrectParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new AddTrainToScheduleCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
