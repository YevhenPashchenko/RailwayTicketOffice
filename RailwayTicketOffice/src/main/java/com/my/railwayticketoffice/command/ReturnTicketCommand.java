package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.ReturnTicketParameterService;
import com.my.railwayticketoffice.service.TrainSearchParameterService;
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
 * Class that built at command pattern. Return ticket that user purchased.
 *
 * @author Yevhen Pashchenko
 */
public class ReturnTicketCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ReturnTicketCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> returnTicketService = new ReturnTicketParameterService();
    private final ParameterService<String> searchTrainService = new TrainSearchParameterService();

    /**
     * Return ticket that user purchased.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @return link to {@link MainPageCommand} or link to {@link GetTrainsCommand} if stations and date chosen.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (user != null) {
            parameters.put("ticketNumber", request.getParameter("ticketNumber"));
            if (returnTicketService.check(parameters, session)) {
                String[] ticketData = parameters.get("ticketNumber").split("-");
                int trainId = Integer.parseInt(ticketData[0]);
                int carriageNumber = Integer.parseInt(ticketData[1]);
                int seatNumber = Integer.parseInt(ticketData[2]);
                String departureDate = ticketData[3].substring(4, 8) + "-" + ticketData[3].substring(2, 4) + "-" + ticketData[3].substring(0, 2);
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int carriageId = trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, trainId, carriageNumber);
                    if (carriageId == 0) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("returnTicketErrorMessage", "Ticket number incorrect");
                        } else {
                            session.setAttribute("returnTicketErrorMessage", "Номер квитка не коректний");
                        }
                        return chooseLink(parameters, session);
                    }
                    Integer userId = scheduleDAO.checkIfSeatBooked(connection, departureDate, trainId, carriageId, seatNumber);
                    if (userId != null && userId == user.getId()) {
                        scheduleDAO.returnTicket(connection, departureDate, trainId, carriageId, seatNumber);
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Ticket has been returned");
                        } else {
                            session.setAttribute("successMessage", "Квиток повернуто");
                        }
                    } else {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("returnTicketErrorMessage", "Ticket already returned");
                        } else {
                            session.setAttribute("returnTicketErrorMessage", "Квиток вже повернуто");
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database to return booked ticket");
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database to return booked ticket");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб повернути замовлений квиток");
                    }
                    throw new DBException("Failed to connect to database to return booked ticket");
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
