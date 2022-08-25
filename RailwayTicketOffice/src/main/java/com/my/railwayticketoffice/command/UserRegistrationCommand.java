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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that built at command pattern. Registers user in database.
 *
 * @author Yevhen Pashchenko
 */
public class UserRegistrationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserRegistrationCommand.class);
    UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private String email;
    private String surname;
    private String name;


    /**
     * Registers user in database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to {@link MainPageCommand} or link to {@link GetTrainsCommand} if stations and date chosen.
     * @throws DBException if {@link SQLException} occurs.
     * @throws AuthenticationException if {@link NoSuchAlgorithmException} or {@link InvalidKeySpecException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, AuthenticationException {
        HttpSession session = request.getSession();
        if (checkParametersForCorrectness(request)) {
            Connection connection = null;
            try {
                String password = PasswordAuthentication.getSaltedHash(request.getParameter("password"));
                connection = DBManager.getInstance().getConnection();
                connection.setAutoCommit(false);
                User user = userDAO.getUser(connection, email);
                if (email.equals(user.getEmail())) {
                    session.setAttribute("errorMessage", "Користувач з такою поштою вже зареєстрований");
                    return chooseLink(request);
                }
                user.setEmail(email);
                user.setPassword(password);
                user.setFirstName(name);
                user.setLastName(surname);
                userDAO.addUser(connection, user);
                connection.commit();
                session.setAttribute("successMessage", "Реєстрація успішна");
                return chooseLink(request);
            } catch (SQLException e) {
                logger.info("Failed to register user in database");
                DBManager.getInstance().rollback(connection, e);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.warn("Failed to get hashed password", e);
                throw new AuthenticationException("Failed to get hashed password");
            } finally {
                DBManager.getInstance().close(connection);
            }
        }
        return chooseLink(request);
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        email = request.getParameter("email");
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        Matcher matcher = pattern.matcher(email);
        if (email.equals("") || !matcher.matches()) {
            session.setAttribute("errorMessage", "Адреса пошти некоректна");
            return false;
        }
        if (!request.getParameter("password").equals(request.getParameter("confirmPassword"))) {
            session.setAttribute("errorMessage", "Введені паролі не співпадають");
            return false;
        }
        surname = request.getParameter("userSurname");
        if (surname.equals("")) {
            session.setAttribute("errorMessage", "Прізвище не має бути пустим");
            return false;
        }
        name = request.getParameter("userName");
        if (name.equals("")) {
            session.setAttribute("errorMessage", "Ім'я не має бути пустим");
            return false;
        }
        return true;
    }

    private String chooseLink(HttpServletRequest request) {
        String link = "controller?command=mainPage";
        int fromStationId;
        int toStationId;
        String date = request.getParameter("datePicker");
        try {
            fromStationId = Integer.parseInt(request.getParameter("from"));
            toStationId = Integer.parseInt(request.getParameter("to"));
            for (String d:
                 date.split("\\.")) {
                Integer.parseInt(d);
            }
        } catch (NumberFormatException e) {
            return "controller?command=mainPage";
        }
        if (date.split("\\.").length == 3) {
            link = "controller?command=getTrains&from=" + fromStationId + "&to=" + toStationId + "&datePicker=" + date;
        }
        return link;
    }
}
