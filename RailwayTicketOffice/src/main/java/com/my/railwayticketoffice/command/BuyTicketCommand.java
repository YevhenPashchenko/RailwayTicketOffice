package com.my.railwayticketoffice.command;

import com.itextpdf.text.DocumentException;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.mail.Mail;
import com.my.railwayticketoffice.mail.MailException;
import com.my.railwayticketoffice.mail.TicketMail;
import com.my.railwayticketoffice.receipt.ReceiptException;
import com.my.railwayticketoffice.service.*;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Class that built at command pattern. Change data in database when user buy tickets.
 *
 * @author Yevhen Pashchenko
 */
public class BuyTicketCommand implements Command {

    private static final Logger logger = LogManager.getLogger(BuyTicketCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> searchTrainService = new SearchTrainParameterService();
    private final ParameterService<String[]> passengerService = new PassengerParameterService();
    private final TicketService ticketService = new TrainTicketService();
    private final Mail mail = new TicketMail();

    /**
     * Change data in database when user buy tickets.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to success.jsp if tickets bought successful or link to {@link MainPageCommand} or {@link GetTrainsCommand}
     * if not.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException, ReceiptException, MailException {
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> passengersData = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (user != null) {
            parameters.put("trainId", request.getParameter("trainId"));
            passengersData.put("surname", request.getParameterValues("passengerSurname"));
            passengersData.put("name", request.getParameterValues("passengerName"));
            if (trainService.check(parameters, session) && passengerService.check(passengersData, session)) {
                Train train;
                int availableSeatsNow;
                List<String> dateForDB = Arrays.asList(parameters.get("date").split("\\."));
                Collections.reverse(dateForDB);
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    train = trainDAO.getTrainSpecifiedByDate(connection, Integer.parseInt(parameters.get("trainId")), String.join("-", dateForDB));
                    availableSeatsNow = train.getSeats() - passengersData.get("surname").length;
                    if (availableSeatsNow >= 0) {
                        trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), "uk");
                        int maxTrainSeats = trainDAO.getTrain(connection, Integer.parseInt(parameters.get("trainId"))).getSeats();
                        user.setTickets(ticketService.create(train, maxTrainSeats, parameters, passengersData));
                    } else {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "There are fewer free seats on the train that the ordered tickets, choose something else");
                        } else {
                            session.setAttribute("errorMessage", "Вільних місць в поїзді менше, ніж замовлених білетів, виберіть щось інше");
                        }
                        return chooseLink(parameters, session);
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database to get the train available seats on this date");
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database to get the train available seats on this date");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати кількість вільних місць  поїзда");
                    }
                    throw new DBException("Failed to connect to database to get the train available seats on this date");
                }
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    connection.setAutoCommit(false);
                    scheduleDAO.changeTrainAvailableSeatsOnThisDate(connection, Integer.parseInt(parameters.get("trainId")), String.join("-", dateForDB), availableSeatsNow);
                    connection.commit();
                    connection.setAutoCommit(true);
                    mail.send(user, session);
                    return "success.jsp";
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for change train available seats on this date");
                    DBManager.getInstance().rollback(session, connection, e);
                } catch (DocumentException e) {
                    logger.info("Failed to create ticket receipt");
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to create ticket receipt");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло створити квитанцію з квитками");
                    }
                    throw new ReceiptException("Failed to create ticket receipt");
                } catch (MessagingException | IOException e) {
                    logger.info("Failed to send email with ticket receipt");
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to send email with ticket receipt");
                    } else {
                        session.setAttribute("errorMessage", "Не вдалося відправити письмо з квитками");
                    }
                    throw new MailException("Failed to send email with ticket receipt");
                } finally {
                    DBManager.getInstance().close(connection);
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
