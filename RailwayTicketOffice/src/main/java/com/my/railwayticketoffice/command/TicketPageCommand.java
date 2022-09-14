package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TicketParameterService;
import com.my.railwayticketoffice.service.TrainSearchParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Class that built at command pattern. Prepare data for ticket.jsp page.
 *
 * @author Yevhen Pashchenko
 */
public class TicketPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(TicketPageCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> searchTrainService = new TrainSearchParameterService();
    private final ParameterService<String[]> ticketParameterService = new TicketParameterService();

    /**
     * Prepare data for ticket.jsp page.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to ticket.jsp or link to {@link MainPageCommand} if incoming data is incorrect.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> ticketParameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        if (user != null) {
            parameters.put("trainId", request.getParameter("trainId"));
            parameters.put("from", request.getParameter("from"));
            parameters.put("to", request.getParameter("to"));
            parameters.put("date", request.getParameter("departureDate"));
            parameters.put("carriageType", request.getParameter("carriageType"));
            ticketParameters.put("carriage", request.getParameterValues("carriage"));
            ticketParameters.put("seat", request.getParameterValues("seat"));
            ticketParameters.put("cost", request.getParameterValues("cost"));
            if (trainService.check(parameters, session) && searchTrainService.check(parameters, session) && ticketParameterService.check(ticketParameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    Train train = trainDAO.getTrain(connection, Integer.parseInt(parameters.get("trainId")));
                    trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), locale);
                    request.setAttribute("train", train);
                    request.setAttribute("from", Integer.parseInt(parameters.get("from")));
                    request.setAttribute("to", Integer.parseInt(parameters.get("to")));
                    request.setAttribute("departureDate", parameters.get("date"));
                    request.setAttribute("carriageType", parameters.get("carriageType"));
                    request.setAttribute("carriage", ticketParameters.get("carriage"));
                    request.setAttribute("seat", ticketParameters.get("seat"));
                    request.setAttribute("cost", ticketParameters.get("cost"));
                    return "ticket.jsp";
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for get train and its route", e);
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database for get train and its route");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати поїзд та його маршрут");
                    }
                    throw new DBException("Failed to connect to database for get train and its route");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
