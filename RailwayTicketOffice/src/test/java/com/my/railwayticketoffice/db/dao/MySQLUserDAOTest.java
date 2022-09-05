package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for methods from {@link MySQLUserDAO}.
 *
 * @author Yevhen Pashchenko
 */
public class MySQLUserDAOTest {

    private final ResultSet rs = mock(ResultSet.class);
    private final PreparedStatement pstmt = mock(PreparedStatement.class);
    private final Connection connection = mock(Connection.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);

    /**
     * Test for method getUser from {@link MySQLUserDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetUser() throws Exception {

        String userEmail = "userEmail";

        User user = new User();
        user.setId(1);
        user.setEmail("userEmail");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setRole("user");

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getUserDAO()).thenReturn(new MySQLUserDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLUserDAOQuery.GET_USER)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("email")).thenReturn("userEmail");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("first_name")).thenReturn("firstName");
        when(rs.getString("last_name")).thenReturn("lastName");
        when(rs.getString("role")).thenReturn("user");

        assertEquals(DBManager.getInstance().getUserDAO().getUser(connection, userEmail), user);
        DBManagerMocked.close();
    }

    /**
     * Test for method addUser from {@link MySQLUserDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testAddUser() throws Exception {

        User user = new User();
        user.setId(1);
        user.setEmail("userEmail");
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getUserDAO()).thenReturn(new MySQLUserDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLUserDAOQuery.ADD_USER)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getUserDAO().addUser(connection, user));
        DBManagerMocked.close();
    }

    /**
     * Test for method updateUserWithPassword from {@link MySQLUserDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testUpdateUser() throws Exception {

        User user = new User();
        user.setId(1);
        user.setPassword("password");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManagerInstance.getUserDAO()).thenReturn(new MySQLUserDAO());
        when(DBManagerInstance.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(MySQLUserDAOQuery.UPDATE_USER)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getUserDAO().updateUser(connection, user));
        DBManagerMocked.close();
    }
}
