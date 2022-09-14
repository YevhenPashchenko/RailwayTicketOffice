package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBManager;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests for methods from {@link AddCarriageTypeCommand}
 *
 * @author Yevhen Pashchenko
 */
public class AddCarriageTypeCommandTest {

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
     * Test for method execute from {@link AddCarriageTypeCommand}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String carriageType = "Тип";
        String maxSeats = "1";

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(request.getParameter("maxSeats")).thenReturn(maxSeats);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.checkIfCarriageTypeExists(connection, carriageType)).thenReturn(0);

        assertEquals("controller?command=mainPage", new AddCarriageTypeCommand().execute(request, response));
        verify(trainDAO, times(1)).addCarriageType(connection, carriageType, Integer.parseInt(maxSeats));
    }

    /**
     * Test for method execute from {@link AddCarriageTypeCommand} when no user in session.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNoUser() throws Exception {

        AddCarriageTypeCommand addCarriageTypeCommand =  new AddCarriageTypeCommand();

        Field field = addCarriageTypeCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(addCarriageTypeCommand, trainService);

        assertEquals("controller?command=mainPage", addCarriageTypeCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link AddCarriageTypeCommand} when incorrect parameters in request.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testExecuteIncorrectParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new AddCarriageTypeCommand().execute(request, response));
        verifyNoInteractions(trainDAO);
    }
}
