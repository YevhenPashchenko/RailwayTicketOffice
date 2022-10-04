package com.my.railwayticketoffice.db.dao;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

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
    MockedStatic<DBManager> DBManagerMocked = Mockito.mockStatic(DBManager.class);
    private final DBManager DBManagerInstance = mock(DBManager.class);

    @BeforeEach
    void beforeEach() throws Exception {
        DBManagerMocked.when((MockedStatic.Verification) DBManager.getInstance()).thenReturn(DBManagerInstance);
        when(DBManager.getInstance().getConnection()).thenReturn(connection);
        when(DBManagerInstance.getUserDAO()).thenReturn(new MySQLUserDAO());
    }

    @AfterEach
    void afterEach() {
        DBManagerMocked.close();
    }

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

        when(connection.prepareStatement(MySQLUserDAOQuery.GET_USER)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getInt("id")).thenReturn(1);
        when(rs.getString("email")).thenReturn("userEmail");
        when(rs.getString("password")).thenReturn("password");
        when(rs.getString("first_name")).thenReturn("firstName");
        when(rs.getString("last_name")).thenReturn("lastName");
        when(rs.getString("role")).thenReturn("user");

        assertEquals(user, DBManager.getInstance().getUserDAO().getUser(connection, userEmail));
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

        when(connection.prepareStatement(MySQLUserDAOQuery.ADD_USER)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getUserDAO().addUser(connection, user));
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

        when(connection.prepareStatement(MySQLUserDAOQuery.UPDATE_USER)).thenReturn(pstmt);
        when(pstmt.executeUpdate()).thenReturn(0);

        assertThrows(SQLException.class, () -> DBManager.getInstance().getUserDAO().updateUser(connection, user));
    }

    /**
     * Test for method getUsersThatPurchasedSeatOnTrain from {@link MySQLUserDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetUsersThatPurchasedSeatOnTrain() throws Exception {

        int trainId = 1;

        User user = new User();
        user.setEmail("userEmail");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        when(connection.prepareStatement(MySQLUserDAOQuery.GET_USERS_THAT_PURCHASED_SEAT_ON_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("email")).thenReturn("userEmail");
        when(rs.getString("first_name")).thenReturn("firstName");
        when(rs.getString("last_name")).thenReturn("lastName");

        assertEquals(Collections.singletonList(user), DBManager.getInstance().getUserDAO().getUsersThatPurchasedSeatOnTrain(connection, trainId));
    }

    /**
     * Test for method getUsersThatPurchasedSeatOnTrainCarriage from {@link MySQLUserDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetUsersThatPurchasedSeatOnTrainCarriage() throws Exception {

        int trainId = 1;
        int carriageId = 1;

        User user = new User();
        user.setEmail("userEmail");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        when(connection.prepareStatement(MySQLUserDAOQuery.GET_USERS_THAT_PURCHASED_SEAT_IN_CARRIAGE_ON_TRAIN)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("email")).thenReturn("userEmail");
        when(rs.getString("first_name")).thenReturn("firstName");
        when(rs.getString("last_name")).thenReturn("lastName");

        assertEquals(Collections.singletonList(user), DBManager.getInstance().getUserDAO().getUsersThatPurchasedSeatOnTrainCarriage(connection, trainId, carriageId));
    }

    /**
     * Test for method getUsersThatPurchasedSeatOnTrainAtDate from {@link MySQLUserDAO}.
     *
     * @throws Exception if any {@link Exception} occurs.
     */
    @Test
    public void testGetUsersThatPurchasedSeatOnTrainAtDate() throws Exception {

        String departureDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int trainId = 1;

        User user = new User();
        user.setEmail("userEmail");
        user.setFirstName("firstName");
        user.setLastName("lastName");

        when(connection.prepareStatement(MySQLUserDAOQuery.GET_USERS_THAT_PURCHASED_SEAT_ON_TRAIN_AT_DATE)).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);
        when(rs.getString("email")).thenReturn("userEmail");
        when(rs.getString("first_name")).thenReturn("firstName");
        when(rs.getString("last_name")).thenReturn("lastName");

        assertEquals(Collections.singletonList(user), DBManager.getInstance().getUserDAO().getUsersThatPurchasedSeatOnTrainAtDate(connection, departureDate, trainId));
    }
}
