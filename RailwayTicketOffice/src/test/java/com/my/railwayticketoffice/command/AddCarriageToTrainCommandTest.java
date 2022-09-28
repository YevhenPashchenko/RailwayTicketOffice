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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link AddCarriageToTrainCommand}
 *
 * @author Yevhen Pashchenko
 */
public class AddCarriageToTrainCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final ParameterService<String> trainService = mock(TrainParameterService.class);
    private final ScheduleService scheduleService = new TrainScheduleService();

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
     * Test for method execute from {@link AddCarriageToTrainCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        int trainId = 1;
        String trainNumber = "Номер";
        int carriageNumber = 1;
        int typeId = 1;
        String carriageType = "Тип";
        int carriageId = 1;
        int maxSeats = 1;

        Map<Integer, String> carriagesTypes = new HashMap<>();
        carriagesTypes.put(typeId, carriageType);

        List<String> scheduleDates = scheduleService.create();

        Train train = new Train();
        train.setId(trainId);
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(carriageId);
        carriage.setMaxSeats(maxSeats);
        train.addCarriage(carriageId, carriage);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn("1");
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("carriageNumber")).thenReturn("1");
        when(request.getParameter("typeId")).thenReturn("1");
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(trainId);
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, trainId, carriageNumber)).thenReturn(0);
        when(trainDAO.getCarriagesTypes(connection)).thenReturn(carriagesTypes);
        when(trainDAO.checkIfCarriageExists(connection, carriageNumber, typeId)).thenReturn(carriageId);
        when(scheduleDAO.checkIfRecordExists(connection, trainId)).thenReturn(true);
        when(trainDAO.getCarriageMaxSeats(connection, carriageId)).thenReturn(maxSeats);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);

        assertEquals("controller?command=mainPage", new AddCarriageToTrainCommand().execute(request, response));
        verify(scheduleDAO, times(1)).addData(connection, scheduleDates, Collections.singletonList(train), null);
    }

    /**
     * Test for method execute from {@link AddCarriageToTrainCommand} when train is not in schedule.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteTrainIsNotInSchedule() throws Exception {
        User user = new User();
        user.setRole("admin");

        int trainId = 1;
        String trainNumber = "Номер";
        int carriageNumber = 1;
        int typeId = 1;
        String carriageType = "Тип";
        int carriageId = 1;
        int maxSeats = 1;

        Map<Integer, String> carriagesTypes = new HashMap<>();
        carriagesTypes.put(typeId, carriageType);

        Train train = new Train();
        train.setId(trainId);
        Train.Carriage carriage = train.new Carriage();
        carriage.setId(carriageId);
        carriage.setMaxSeats(maxSeats);
        train.addCarriage(carriageId, carriage);


        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn("1");
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("carriageNumber")).thenReturn("1");
        when(request.getParameter("typeId")).thenReturn("1");
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(trainId);
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, trainId, carriageNumber)).thenReturn(0);
        when(trainDAO.getCarriagesTypes(connection)).thenReturn(carriagesTypes);
        when(trainDAO.checkIfCarriageExists(connection, carriageNumber, typeId)).thenReturn(carriageId);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(scheduleDAO.checkIfRecordExists(connection, trainId)).thenReturn(false);

        assertEquals("controller?command=mainPage", new AddCarriageToTrainCommand().execute(request, response));
        verify(trainDAO, times(1)).addCarriageToTrain(connection, trainId, carriageId);
    }

    /**
     * Test for method execute from {@link AddCarriageToTrainCommand} when no user in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNoUser() throws Exception {
        AddCarriageToTrainCommand addCarriageToTrainCommand = new AddCarriageToTrainCommand();

        Field field = addCarriageToTrainCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(addCarriageToTrainCommand, trainService);

        assertEquals("controller?command=mainPage", addCarriageToTrainCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link AddCarriageToTrainCommand} when incorrect parameters in request.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new AddCarriageToTrainCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
