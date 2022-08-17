package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.authentication.AuthenticationException;
import com.my.railwayticketoffice.authentication.PasswordAuthentication;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that built at command pattern. Authenticate user and save him in session on login.
 *
 * @author Yevhen Pashchenko
 */
public class UserLoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserLoginCommand.class);
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();

    /**
     * Authenticate user and save him in session on login.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to {@link MainPageCommand} or link to {@link GetTrainsCommand} if stations and date chosen.
     * @throws DBException if {@link SQLException} occurs.
     * @throws AuthenticationException if {@link NoSuchAlgorithmException} or {@link InvalidKeySpecException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, AuthenticationException {
        HttpSession session = request.getSession();
        String userEmail = request.getParameter("email");
        try(Connection connection = DBManager.getInstance().getConnection()) {
            User user = userDAO.getUser(connection, userEmail);
            if (userEmail.equals(user.getEmail()) && PasswordAuthentication.check(request.getParameter("password"), user.getPassword())) {
                user.setPassword("");
                session.setAttribute("user", user);
                return chooseLink(request);
            }
            session.setAttribute("loginErrorMessage", "Помилкова пошта або пароль");
            return chooseLink(request);
        } catch (SQLException e) {
            logger.warn("Failed to authenticate user", e);
            throw new DBException("Failed to authenticate user");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warn("Failed to authenticate user", e);
            throw new AuthenticationException("Failed to authenticate user");
        }
    }

    private String chooseLink(HttpServletRequest request) {
        String link;
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String datePicker = request.getParameter("datePicker");
        if (!from.equals("") && !to.equals("")) {
            link = "controller?command=getTrains&from=" + from + "&to=" + to + "&datePicker=" + datePicker;
        } else {
            link = "controller?command=mainPage";
        }
        return link;
    }
}
