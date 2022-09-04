package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.authentication.AuthenticationException;
import com.my.railwayticketoffice.authentication.PasswordAuthentication;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.Mail;
import com.my.railwayticketoffice.mail.RegistrationMail;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.SearchTrainParameterService;
import com.my.railwayticketoffice.service.UserParameterService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Registers user in database.
 *
 * @author Yevhen Pashchenko
 */
public class UserRegistrationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserRegistrationCommand.class);
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final Mail mail = new RegistrationMail();
    private final ParameterService<String> userService = new UserParameterService();
    private final ParameterService<String> searchTrainService = new SearchTrainParameterService();


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
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("locale");
        User user;
        parameters.put("email", request.getParameter("email"));
        parameters.put("password", request.getParameter("password"));
        parameters.put("confirmPassword", request.getParameter("confirmPassword"));
        parameters.put("surname", request.getParameter("userSurname"));
        parameters.put("name", request.getParameter("userName"));
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (userService.check(parameters, session)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                user = userDAO.getUser(connection, parameters.get("email"));
                if (parameters.get("email").equals(user.getEmail())) {
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "User with this email is already registered");
                    } else {
                        session.setAttribute("errorMessage", "Користувач з такою поштою вже зареєстрований");
                    }
                    return chooseLink(parameters, session);
                }
                user.setEmail(parameters.get("email"));
                user.setPassword(PasswordAuthentication.getSaltedHash(parameters.get("password")));
                user.setLastName(parameters.get("surname"));
                user.setFirstName(parameters.get("name"));
            } catch (SQLException e) {
                logger.info("Failed to connect to database to verify the user data", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database to verify the user data");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб перевірити дані користувача");
                }
                throw new DBException("Failed to connect to database for edit user data in database");
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.info("Failed to encrypt password", e);
                if ("en".equals(locale)) {
                    session.setAttribute("errorMessage", "Failed to encrypt password");
                } else {
                    session.setAttribute("errorMessage", "Не вдалося зашифрувати пароль");
                }
                throw new AuthenticationException("Failed to encrypt password");
            }
            Connection connection = null;
            try {
                connection = DBManager.getInstance().getConnection();
                connection.setAutoCommit(false);
                userDAO.addUser(connection, user);
                mail.send(user.getEmail(), locale);
                connection.commit();
                connection.setAutoCommit(true);
                if ("en".equals(locale)) {
                    session.setAttribute("successMessage", "To complete the registration, follow the link in the letter sent to the mail");
                } else {
                    session.setAttribute("successMessage", "Для завершення реєстрації перейдіть по ссилці, яка знаходиться в письмі, що вислано на пошту");
                }
                return chooseLink(parameters, session);
            } catch (SQLException e) {
                logger.info("Failed to connect to database for add user in database");
                DBManager.getInstance().rollback(session, connection, e);
            } catch (IOException | MessagingException e) {
                logger.info("Failed to send email to confirm registration");
                DBManager.getInstance().rollback(session, connection, new SQLException(e));
            } finally {
                DBManager.getInstance().close(connection);
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
