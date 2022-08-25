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
 * Class that built at command pattern. Edit user data in database.
 *
 * @author Yevhen Pashchenko
 */
public class UserEditCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserEditCommand.class);
    UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private String password;
    private String surname;
    private String name;

    /**
     * Edit user data in database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to {@link MainPageCommand} or link to {@link GetTrainsCommand} if stations and date chosen.
     * @throws DBException if {@link SQLException} occurs.
     * @throws AuthenticationException if {@link NoSuchAlgorithmException} or {@link InvalidKeySpecException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, AuthenticationException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                if (surname != null && !surname.equals(user.getLastName())) {
                    user.setLastName(surname);
                }
                if (name != null && !name.equals(user.getFirstName())) {
                    user.setFirstName(name);
                }
                if (password != null && !password.equals("")) {
                    user.setPassword(PasswordAuthentication.getSaltedHash(password));
                    userDAO.updateUserWithPassword(connection, user);
                } else {
                    userDAO.updateUser(connection, user);
                }
                session.setAttribute("successMessage", "Дані користувача змінено успішно");
                return chooseLink(request);
            } catch (SQLException e) {
                logger.info("Failed to edit user data in database", e);
                throw new DBException("Failed to edit user data in database");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.info("Failed to get hashed password", e);
                throw new AuthenticationException("Failed to get hashed password");
            }
        }
        return chooseLink(request);
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        if (password != null && !password.equals(confirmPassword)) {
            session.setAttribute("errorMessage", "Введені паролі не співпадають");
            return false;
        }
        surname = request.getParameter("userSurname");
        if (surname != null && surname.equals("")) {
            session.setAttribute("errorMessage", "Прізвище не має бути пустим");
            return false;
        }
        name = request.getParameter("userName");
        if (name != null && name.equals("")) {
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
