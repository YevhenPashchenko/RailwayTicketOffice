package com.my.railwayticketoffice.command;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.db.dao.UserDAO;
import com.my.railwayticketoffice.entity.Ticket;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.EditCarriageNumberInTrainMail;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that built at command pattern. Edit carriage number in train.
 *
 * @author Yevhen Pashchenko
 */
public class EditCarriageNumberInTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditCarriageNumberInTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final UserDAO userDAO = DBManager.getInstance().getUserDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final Mail mail = new EditCarriageNumberInTrainMail();

    /**
     * Edit carriage number in train.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
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
            parameters.put("carriageId", request.getParameter("carriageId"));
            parameters.put("carriageNumber", request.getParameter("carriageNumber"));
            parameters.put("newCarriageNumber", request.getParameter("newCarriageNumber"));
            parameters.put("typeId", request.getParameter("typeId"));
            parameters.put("carriageType", request.getParameter("carriageType"));
            if (trainService.check(parameters, session)) {
                int newCarriageId;
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isTrainExist = trainDAO.checkIfTrainExists(connection, parameters.get("trainNumber"));
                    if (isTrainExist == 0 || isTrainExist != Integer.parseInt(parameters.get("trainId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "Train with this number is not specified");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    Map<Integer, String> carriagesTypes = trainDAO.getCarriagesTypes(connection);
                    int typeId = Integer.parseInt(parameters.get("typeId"));
                    if (!carriagesTypes.containsKey(typeId) &&
                            !parameters.get("carriageType").equals(carriagesTypes.get(typeId))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "Carriage with this type is not specified");
                        } else {
                            session.setAttribute("errorMessage", "Вагона такого типу не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    int isTrainHasCarriageWithThisNumber = trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageNumber")));
                    if (isTrainHasCarriageWithThisNumber == 0 || isTrainHasCarriageWithThisNumber != Integer.parseInt(parameters.get("carriageId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "There is no carriage with this number in the train");
                        } else {
                            session.setAttribute("errorMessage", "В поїзді немає вагона з таким номером");
                        }
                        return "controller?command=mainPage";
                    }
                    int isTrainAlreadyHasCarriageWithNewNumber = trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("newCarriageNumber")));
                    if (isTrainAlreadyHasCarriageWithNewNumber == 0) {
                        int isCarriageExists = trainDAO.checkIfCarriageExists(connection, Integer.parseInt(parameters.get("newCarriageNumber")), typeId);
                        if (isCarriageExists == 0) {
                            newCarriageId = trainDAO.createCarriage(connection, Integer.parseInt(parameters.get("newCarriageNumber")), typeId);
                        } else {
                            newCarriageId = isCarriageExists;
                        }
                    } else {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "The train already has a carriage with this number");
                        } else {
                            session.setAttribute("errorMessage", "В поїзді вже є вагон з таким номером");
                        }
                        return "controller?command=mainPage";
                    }
                    if (!scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(parameters.get("trainId")))) {
                        trainDAO.editCarriageNumberInTrain(connection, newCarriageId, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageId")));
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Carriage number in train has been edited");
                        } else {
                            session.setAttribute("successMessage", "Номер вагона в поїзді змінено");
                        }
                        return "controller?command=mainPage";
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for edit carriage number in train");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for edit carriage number in train");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб редагувати номер вагона в поїзді");
                    }
                    throw new DBException("Failed to connect to database for edit carriage number in train");
                }
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    List<User> users = userDAO.getUsersThatPurchasedSeatOnTrainCarriage(connection, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageId")));
                    if (users.size() > 0) {
                        Ticket ticket = new Ticket();
                        ticket.setTrainNumber(parameters.get("trainNumber"));
                        ticket.setCarriageNumber(Integer.parseInt(parameters.get("carriageNumber")));
                        Ticket newTicket = new Ticket();
                        newTicket.setCarriageNumber(Integer.parseInt(parameters.get("newCarriageNumber")));
                        for (User u:
                                users) {
                            u.setTickets(Arrays.asList(ticket, newTicket));
                        }
                        mail.send(users, session);
                    }
                    connection.setAutoCommit(false);
                    trainDAO.editCarriageNumberInTrain(connection, newCarriageId, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageId")));
                    scheduleDAO.editCarriageData(connection, newCarriageId, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageId")));
                    connection.setAutoCommit(true);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "Carriage number in train has been edited");
                    } else {
                        session.setAttribute("successMessage", "Номер вагона в поїзді змінено");
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for edit carriage number in train");
                    DBManager.getInstance().rollback(session, connection, e);
                } catch (IOException | MessagingException e) {
                    logger.info("Failed to send mails to users about change train carriage number");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to send mails to users about change train carriage number");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло відправити письма користувачам про зміну номера вагона поїзда");
                    }
                    throw new MailException("Failed to send mails to users about change train carriage number");
                } catch (DocumentException ignored) {
                } finally {
                    DBManager.getInstance().close(connection);
                }
            }
        }
        return "controller?command=mainPage";
    }
}
