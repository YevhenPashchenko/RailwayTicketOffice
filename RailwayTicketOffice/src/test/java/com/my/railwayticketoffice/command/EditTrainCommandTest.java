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
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link EditTrainCommand}
 *
 * @author Yevhen Pashchenko
 */
public class EditTrainCommandTest {

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
     * Test for method execute from {@link EditTrainCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String trainId = "1";
        String trainNumber = "Номер";
        String oldTrainNumber = "Старий номер";
        String trainDepartureTime = "00:00";

        Train train = new Train();
        train.setId(Integer.parseInt(trainId));
        train.setNumber(trainNumber);
        train.setDepartureTime(LocalTime.parse(trainDepartureTime));

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("trainId")).thenReturn(trainId);
        when(request.getParameter("trainNumber")).thenReturn(trainNumber);
        when(request.getParameter("oldTrainNumber")).thenReturn(oldTrainNumber);
        when(request.getParameter("trainDepartureTime")).thenReturn(trainDepartureTime);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.checkIfTrainExists(connection, oldTrainNumber)).thenReturn(1);

        assertEquals("controller?command=mainPage", new EditTrainCommand().execute(request, response));
        verify(trainDAO, times(1)).editTrain(connection, train);
    }

    /**
     * Test for method execute from {@link EditTrainCommand} when user is not in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        EditTrainCommand editTrainCommand = new EditTrainCommand();

        Field field = editTrainCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(editTrainCommand, trainService);

        assertEquals("controller?command=mainPage", editTrainCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link EditTrainCommand} when not parameters.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new EditTrainCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
