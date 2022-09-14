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
 * Class that built at command pattern. Edit user data in database.
 *
 * @author Yevhen Pashchenko
 */
public class UserEditCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserEditCommand.class);
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final ParameterService<String> userService = new UserParameterService();
    private final ParameterService<String> searchTrainService = new TrainSearchParameterService();

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
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (user != null) {
            parameters.put("surname", request.getParameter("userSurname"));
            parameters.put("name", request.getParameter("userName"));
            if (userService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    User userNewData = new User();
                    userNewData.setId(user.getId());
                    userNewData.setLastName(parameters.get("surname"));
                    userNewData.setFirstName(parameters.get("name"));
                    userNewData.setRegistered(user.isRegistered());
                    parameters.put("password", request.getParameter("password"));
                    parameters.put("confirmPassword", request.getParameter("confirmPassword"));
                    if (userService.check(parameters, session)) {
                        userNewData.setPassword(PasswordAuthentication.getSaltedHash(parameters.get("password")));
                        userDAO.updateUser(connection, userNewData);
                    } else {
                        if (parameters.get("password") != null && !"".equals(parameters.get("password")) ||
                                parameters.get("confirmPassword") != null && !"".equals(parameters.get("confirmPassword"))) {
                            return chooseLink(parameters, session);
                        }
                        session.removeAttribute("userErrorMessage");
                        User userDataFromDB = userDAO.getUser(connection, user.getEmail());
                        userNewData.setPassword(userDataFromDB.getPassword());
                        userDAO.updateUser(connection, userNewData);
                        session.setAttribute("user", userNewData);
                    }
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "User data has been edited");
                    } else {
                        session.setAttribute("successMessage", "Дані користувача змінено");
                    }
                    return chooseLink(parameters, session);
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for edit user data in database", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for edit user data in database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб відредагувати дані користувача");
                    }
                    throw new DBException("Failed to connect to database for edit user data in database");
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    logger.info("Failed to encrypt new password", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to encrypt new password");
                    } else {
                        session.setAttribute("errorMessage", "Не вдалося зашифрувати новий пароль");
                    }
                    throw new AuthenticationException("Failed to encrypt new password");
                }
            }
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
