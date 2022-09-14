package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.authentication.AuthenticationException;
import com.my.railwayticketoffice.authentication.PasswordAuthentication;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainSearchParameterService;
import com.my.railwayticketoffice.service.UserParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Authenticate user and save him in session on login.
 *
 * @author Yevhen Pashchenko
 */
public class UserLoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserLoginCommand.class);
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final ParameterService<String> userService = new UserParameterService();
    private final ParameterService<String> searchTrainService = new TrainSearchParameterService();

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
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        parameters.put("email", request.getParameter("email"));
        parameters.put("password", request.getParameter("password"));
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (userService.check(parameters, session)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                User user = userDAO.getUser(connection, parameters.get("email"));
                if (parameters.get("email").equals(user.getEmail()) && PasswordAuthentication.check(parameters.get("password"), user.getPassword())) {
                    if (!user.isRegistered()) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "User has not finished registration");
                        } else {
                            session.setAttribute("errorMessage", "Користувач не закінчив реєстрацію");
                        }
                        return chooseLink(parameters, session);
                    }
                    user.setPassword("");
                    session.setAttribute("user", user);
                    return chooseLink(parameters, session);
                }
            } catch (SQLException e) {
                logger.warn("Failed to connect to database for get user data in database", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database for get user data in database");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати дані користувача");
                }
                throw new DBException("Failed to connect to database for get user data in database");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.warn("Password decryption error", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Password decryption error");
                } else {
                    session.setAttribute("errorMessage", "Помилка при розшифровці пароля");
                }
                throw new AuthenticationException("Password decryption error");
            }
        }
        if ("en".equals(session.getAttribute("locale"))) {
            session.setAttribute("errorMessage", "Email or password is wrong");
        } else {
            session.setAttribute("errorMessage", "Помилкова пошта або пароль");
        }
        return chooseLink(parameters, session);
    }

    private String chooseLink(Map<String, String> parameters, HttpSession session) {
        if (searchTrainService.check(parameters, session)) {
            return "controller?command=getTrains&from=" + parameters.get("from") + "&to=" + parameters.get("to") + "&departureDate=" + parameters.get("date");
        } else {
            session.removeAttribute("searchTrainErrorMessage");
            return "controller?command=mainPage";
        }
    }
}
