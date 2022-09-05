package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Tests for methods from {@link EditCarriageNumberInTrainCommand}
 *
 * @author Yevhen Pashchenko
 */
public class EditCarriageNumberInTrainCommandTest {

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpSession session = mock(HttpSession.class);
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);
    private final Connection connection = mock(Connection.class);
    private final TrainDAO trainDAO = mock(TrainDAO.class);
    private final ScheduleDAO scheduleDAO = mock(ScheduleDAO.class);
    private final ParameterService<String> trainParameterService = mock(TrainParameterService.class);

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
     * Test for method execute from {@link EditCarriageNumberInTrainCommand}.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");
        String trainId = "1";
        String trainNumber = "Номер";
        String carriageId = "1";
        String carriageNumber = "1";
        String newCarriageNumber = "2";
        String typeId = "1";
        String carriageType = "Тип";

        Map<Integer, String> carriagesTypes = new HashMap<>();
        carriagesTypes.put(Integer.parseInt(typeId), carriageType);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("carriageId")).thenReturn(carriageId);
        when(request.getParameter("carriageNumber")).thenReturn(carriageNumber);
        when(request.getParameter("newCarriageNumber")).thenReturn(newCarriageNumber);
        when(request.getParameter("typeId")).thenReturn(typeId);
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(Integer.parseInt(trainId));
        when(trainDAO.getCarriagesTypes(connection)).thenReturn(carriagesTypes);
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(trainId), Integer.parseInt(carriageNumber))).thenReturn(Integer.parseInt(carriageId));
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(trainId), Integer.parseInt(newCarriageNumber))).thenReturn(0);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(trainId))).thenReturn(true);

        assertEquals("controller?command=mainPage", new EditCarriageNumberInTrainCommand().execute(request, response));
        verify(scheduleDAO, times(1)).editCarriageData(connection, Integer.parseInt(trainId), 0);
    }

    /**
     * Test for method execute from {@link EditCarriageNumberInTrainCommand} when train is not in schedule.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteTrainNotInSchedule() throws Exception {
        User user = new User();
        user.setRole("admin");
        String trainId = "1";
        String trainNumber = "Номер";
        String carriageId = "1";
        String carriageNumber = "1";
        String newCarriageNumber = "2";
        String typeId = "1";
        String carriageType = "Тип";

        Map<Integer, String> carriagesTypes = new HashMap<>();
        carriagesTypes.put(Integer.parseInt(typeId), carriageType);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("carriageId")).thenReturn(carriageId);
        when(request.getParameter("carriageNumber")).thenReturn(carriageNumber);
        when(request.getParameter("newCarriageNumber")).thenReturn(newCarriageNumber);
        when(request.getParameter("typeId")).thenReturn(typeId);
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.checkIfTrainExists(connection, trainNumber)).thenReturn(Integer.parseInt(trainId));
        when(trainDAO.getCarriagesTypes(connection)).thenReturn(carriagesTypes);
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(trainId), Integer.parseInt(carriageNumber))).thenReturn(Integer.parseInt(carriageId));
        when(trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(trainId), Integer.parseInt(newCarriageNumber))).thenReturn(0);
        when(DBManager.getInstance().getScheduleDAO()).thenReturn(scheduleDAO);
        when(scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(trainId))).thenReturn(false);

        assertEquals("controller?command=mainPage", new EditCarriageNumberInTrainCommand().execute(request, response));
        verify(scheduleDAO, times(0)).editCarriageData(connection, Integer.parseInt(trainId), 0);
    }

    /**
     * Test for method execute from {@link EditCarriageNumberInTrainCommand} when user is not in session.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        EditCarriageNumberInTrainCommand editCarriageNumberInTrainCommand = new EditCarriageNumberInTrainCommand();

        Field field = editCarriageNumberInTrainCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(editCarriageNumberInTrainCommand, trainParameterService);

        assertEquals("controller?command=mainPage", editCarriageNumberInTrainCommand.execute(request, response));
        verifyNoInteractions(trainParameterService);
    }

    /**
     * Test for method execute from {@link EditCarriageNumberInTrainCommand} when incorrect parameters in request.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteIncorrectParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new EditCarriageNumberInTrainCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
