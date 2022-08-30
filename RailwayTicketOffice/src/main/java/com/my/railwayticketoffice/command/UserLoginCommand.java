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
import java.util.Arrays;
import java.util.List;

/**
 * Class that built at command pattern. Authenticate user and save him in session on login.
 *
 * @author Yevhen Pashchenko
 */
public class UserLoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserLoginCommand.class);
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private int fromStationId;
    private int toStationId;
    private String formattedDate;

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
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Email or password is wrong");
            } else {
                session.setAttribute("errorMessage", "Помилкова пошта або пароль");
            }
            return chooseLink(request);
        } catch (SQLException e) {
            logger.warn("Failed to connect to database for get user data in database", e);
            if ("en".equals(session.getAttribute("locale"))) {
                throw new DBException("Failed to connect to database for get user data in database");
            } else {
                throw new DBException("Не вийшло зв'язатися з базою даних, щоб отримати дані користувача");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.warn("Password decryption error", e);
            if ("en".equals(session.getAttribute("locale"))) {
                throw new AuthenticationException("Password decryption error");
            } else {
                throw new AuthenticationException("Помилка при розшифровці пароля");
            }
        }
    }

    private String chooseLink(HttpServletRequest request) {
        String link;
        if (checkParametersForCorrectness(request)) {
            link = "controller?command=getTrains&from=" + fromStationId + "&to=" + toStationId + "&datePicker=" + formattedDate;
        } else {
            link = "controller?command=mainPage";
        }
        return link;
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        try {
            fromStationId = Integer.parseInt(request.getParameter("from"));
            toStationId = Integer.parseInt(request.getParameter("to"));
        } catch (NumberFormatException e) {
            return false;
        }
        if (fromStationId == toStationId) {
            return false;
        }
        List<String> date = Arrays.asList(request.getParameter("datePicker").split("\\."));
        if (date.size() != 3) {
            return false;
        }
        for (String d:
                date) {
            try {
                Integer.parseInt(d);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        formattedDate = String.join(".", date);
        return true;
    }
}
