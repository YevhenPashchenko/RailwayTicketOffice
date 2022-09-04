package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.PassengerParameterService;
import com.my.railwayticketoffice.service.SearchTrainParameterService;
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
 * Class that built at command pattern. Change data in database when user buy tickets.
 *
 * @author Yevhen Pashchenko
 */
public class BuyTicketCommand implements Command {

    private static final Logger logger = LogManager.getLogger(BuyTicketCommand.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> searchTrainService = new SearchTrainParameterService();
    private final ParameterService<String[]> passengerService = new PassengerParameterService();

    /**
     * Change data in database when user buy tickets.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to success.jsp if tickets bought successful or link to {@link MainPageCommand} or {@link GetTrainsCommand}
     * if not.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> passengersData = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (user != null) {
            parameters.put("trainId", request.getParameter("trainId"));
            passengersData.put("surname", request.getParameterValues("passengerSurname"));
            passengersData.put("name", request.getParameterValues("passengerName"));
            if (trainService.check(parameters, session) && passengerService.check(passengersData, session)) {
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    connection.setAutoCommit(false);
                    List<String> dateForDB = Arrays.asList(parameters.get("date").split("\\."));
                    Collections.reverse(dateForDB);
                    int availableSeats = scheduleDAO.getTrainAvailableSeatsOnThisDate(connection, Integer.parseInt(parameters.get("trainId")), String.join("-", dateForDB));
                    int availableSeatsNow = availableSeats - passengersData.get("surname").length;
                    if (availableSeatsNow >= 0) {
                        scheduleDAO.changeTrainAvailableSeatsOnThisDate(connection, Integer.parseInt(parameters.get("trainId")), String.join("-", dateForDB), availableSeatsNow);
                        connection.commit();
                        connection.setAutoCommit(true);
                        return "success.jsp";
                    }
                    connection.commit();
                    connection.setAutoCommit(true);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "There are fewer free seats on the train that the ordered tickets, choose something else");
                    } else {
                        session.setAttribute("errorMessage", "Вільних місць в поїзді менше, ніж замовлених білетів, виберіть щось інше");
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for change train available seats on this date");
                    DBManager.getInstance().rollback(session, connection, e);
                } finally {
                    DBManager.getInstance().close(connection);
                }
            }
        }
        if (searchTrainService.check(parameters, session)) {
            return "controller?command=getTrains&from=" + Integer.parseInt(parameters.get("from")) + "&to=" + Integer.parseInt(parameters.get("to")) + "&departureDate=" + parameters.get("date");
        } else {
            session.removeAttribute("searchTrainErrorMessage");
            return "controller?command=mainPage";
        }
    }
}
