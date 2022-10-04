package com.my.railwayticketoffice.command;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.EditTrainMail;
import com.my.railwayticketoffice.mail.Mail;
import com.my.railwayticketoffice.mail.MailException;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that built at command pattern. Edit train data in database.
 *
 * @author Yevhen Pashchenko
 */
public class EditTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final Mail mail = new EditTrainMail();

    /**
     * Edit train data in database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, MailException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("trainId", request.getParameter("trainId"));
            parameters.put("trainNumber", request.getParameter("trainNumber"));
            parameters.put("trainDepartureTime", request.getParameter("trainDepartureTime"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int trainId = Integer.parseInt(parameters.get("trainId"));
                    int isTrainExists = trainDAO.checkIfTrainExists(connection, request.getParameter("oldTrainNumber"));
                    if (isTrainExists == 0 || isTrainExists != trainId) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "A train with this number no exists");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                    } else {
                        Train train = trainDAO.getTrain(connection, trainId);
                        List<User> users = userDAO.getUsersThatPurchasedSeatOnTrain(connection, trainId);
                        if (users.size() > 0) {
                            Ticket ticket = new Ticket();
                            ticket.setTrainNumber(train.getNumber());
                            ticket.setDepartureDateTime(LocalDateTime.of(LocalDate.now(), train.getDepartureTime()));
                            Ticket newTicket = new Ticket();
                            newTicket.setTrainNumber(request.getParameter("trainNumber"));
                            newTicket.setDepartureDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.parse(parameters.get("trainDepartureTime"))));
                            for (User u:
                                    users) {
                                u.setTickets(Arrays.asList(ticket, newTicket));
                            }
                            mail.send(users, session);
                        }
                        train.setNumber(parameters.get("trainNumber"));
                        train.setDepartureTime(LocalTime.parse(parameters.get("trainDepartureTime")));
                        trainDAO.editTrain(connection, train);
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Train data has been edited");
                        } else {
                            session.setAttribute("successMessage", "Дані поїзда змінено");
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for edit train data in database");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for edit train data in database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб відредагувати дані поїзда");
                    }
                    throw new DBException("Failed to connect to database for edit train data in database");
                } catch (IOException | MessagingException e) {
                    logger.info("Failed to send mails to users about change train data");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to send mail to users about change train data");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло відправити письма користувачам про зміну даних поїзда");
                    }
                    throw new MailException("Failed to send mails to users about change train data");
                } catch (DocumentException ignored) {
                }
            }
        }
        return "controller?command=mainPage";
    }
}
