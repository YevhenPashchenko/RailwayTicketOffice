package com.my.railwayticketoffice.command;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.DeleteTrainFromScheduleMail;
import com.my.railwayticketoffice.mail.Mail;
import com.my.railwayticketoffice.mail.MailException;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import com.my.railwayticketoffice.service.TrainSearchParameterService;
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
import java.util.*;

/**
 * Class that built at command pattern. Delete train from schedule.
 */
public class DeleteTrainFromScheduleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteTrainFromScheduleCommand.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> searchTrainService = new TrainSearchParameterService();
    private final Mail mail = new DeleteTrainFromScheduleMail();

    /**
     * Delete train from schedule.
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
        String locale = (String) session.getAttribute("locale");
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("trainId", request.getParameter("trainId"));
            parameters.put("trainNumber", request.getParameter("trainNumber"));
            parameters.put("date", request.getParameter("departureDate"));
            if (trainService.check(parameters, session) && searchTrainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int trainId = Integer.parseInt(parameters.get("trainId"));
                    List<String> dateForDB = Arrays.asList(parameters.get("date").split("\\."));
                    Collections.reverse(dateForDB);
                    if (!scheduleDAO.checkIfRecordExists(connection, trainId)) {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "There is no train on the schedule");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда немає в розкладі");
                        }
                        return "controller?command=mainPage";
                    }
                    int isTrainExists = trainDAO.checkIfTrainExists(connection, parameters.get("trainNumber"));
                    if (isTrainExists == 0 || isTrainExists != trainId) {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "A train with this number no exists");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    if (scheduleDAO.checkIfTrainHasBookedSeatsAtDate(connection, String.join("-", dateForDB), trainId)) {
                        List<User> users = userDAO.getUsersThatPurchasedSeatOnTrainAtDate(connection, String.join("-", dateForDB), trainId);
                        Ticket ticket = new Ticket();
                        ticket.setTrainNumber(parameters.get("trainNumber"));
                        ticket.setDepartureDateTime(LocalDateTime.of(LocalDate.parse(String.join("-", dateForDB)), LocalTime.MIDNIGHT));
                        for (User u:
                                users) {
                            u.setTickets(Collections.singletonList(ticket));
                        }
                        mail.send(users, session);
                    }
                    scheduleDAO.deleteTrainFromScheduleAtDate(connection, String.join("-", dateForDB), trainId);
                    if ("en".equals(locale)) {
                        session.setAttribute("successMessage", "Train has been deleted from schedule");
                    } else {
                        session.setAttribute("successMessage", "Поїзд видалено з розкладу");
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for delete train from schedule", e);
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database for delete train from schedule");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити поїзд з розкладу");
                    }
                    throw new DBException("Failed to connect to database for delete train from schedule");
                } catch (IOException | MessagingException e) {
                    logger.info("Failed to send mails to users about delete train from schedule");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to send mails to users about delete train from schedule");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло відправити письма користувачам про видалення поїзда з розкладу");
                    }
                    throw new MailException("Failed to send mails to users about delete train from schedule");
                } catch (DocumentException ignored) {
                }
            }
        }
        return "controller?command=mainPage";
    }
}
