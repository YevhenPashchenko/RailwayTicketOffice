package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.UserParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Confirm registration user.
 *
 * @author Yevhen Pashchenko
 */
public class ConfirmRegistrationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ConfirmRegistrationCommand.class);
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final ParameterService<String> userService = new UserParameterService();

    /**
     * Confirm registration user.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("locale");
        parameters.put("email", request.getParameter("email"));
        if (userService.check(parameters, session)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                User user = userDAO.getUser(connection, parameters.get("email"));
                if (!user.isRegistered()) {
                    user.setRegistered(true);
                    userDAO.updateUser(connection, user);
                    if ("en".equals(locale)) {
                        session.setAttribute("successMessage", "Registration complete");
                    } else {
                        session.setAttribute("successMessage", "Реєстрація успішна");
                    }
                } else {
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "User is already registered");
                    } else {
                        session.setAttribute("errorMessage", "Користувач вже зареєстрований");
                    }
                }
                return "controller?command=mainPage";
            } catch (SQLException e) {
                logger.info("Failed to connect to database to confirm register user", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database to confirm register user");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб підтвердити реєстрацію користувача");
                }
                throw new DBException("Failed to connect to database to confirm register user");
            }
        }
        if ("en".equals(locale)) {
            session.setAttribute("errorMessage", "Failed to confirm registration user, try again");
        } else {
            session.setAttribute("errorMessage", "Не вдалося зареєструвати користувача, спробуйте знову");
        }
        return "controller?command=mainPage";
    }
}
