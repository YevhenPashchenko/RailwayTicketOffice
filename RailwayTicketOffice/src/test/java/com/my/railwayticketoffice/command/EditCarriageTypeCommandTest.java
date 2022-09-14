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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Tests for methods from {@link EditCarriageTypeCommand}
 *
 * @author Yevhen Pashchenko
 */
public class EditCarriageTypeCommandTest {

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
     * Test for method execute from {@link EditCarriageTypeCommand}.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecute() throws Exception {
        User user = new User();
        user.setRole("admin");

        String typeId = "1";
        String carriageType = "Тип";
        String newCarriageType = "Новий тип";

        Map<Integer, String> types = new HashMap<>();
        types.put(1, carriageType);

        when((User) session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("typeId")).thenReturn(typeId);
        when(request.getParameter("carriageType")).thenReturn(carriageType);
        when(request.getParameter("newCarriageType")).thenReturn(newCarriageType);
        when(DBManager.getInstance().getTrainDAO()).thenReturn(trainDAO);
        when(trainDAO.getCarriagesTypes(connection)).thenReturn(types);

        assertEquals("controller?command=mainPage", new EditCarriageTypeCommand().execute(request, response));
        verify(trainDAO, times(1)).editCarriageType(connection, newCarriageType, Integer.parseInt(typeId));
    }

    /**
     * Test for method execute from {@link EditCarriageTypeCommand} when user is not in session.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotUserInSession() throws Exception {

        EditCarriageTypeCommand editCarriageTypeCommand = new EditCarriageTypeCommand();

        Field field = editCarriageTypeCommand.getClass().getDeclaredField("trainService");
        field.setAccessible(true);
        field.set(editCarriageTypeCommand, trainService);

        assertEquals("controller?command=mainPage", editCarriageTypeCommand.execute(request, response));
        verifyNoInteractions(trainService);
    }

    /**
     * Test for method execute from {@link EditCarriageTypeCommand} when not parameters.
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    void testExecuteNotParameters() throws Exception {
        User user = new User();
        user.setRole("admin");

        when((User) session.getAttribute("user")).thenReturn(user);

        assertEquals("controller?command=mainPage", new EditCarriageTypeCommand().execute(request, response));
        verifyNoInteractions(connection);
    }
}
