package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.entity.User;
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
    private int trainId;
    private String date;
    private Map<String, String[]> passengersData = new HashMap<>();

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && checkParametersForCorrectness(request)) {
            Connection connection = null;
            try {
                connection = DBManager.getInstance().getConnection();
                connection.setAutoCommit(false);
                int availableSeats = scheduleDAO.getTrainAvailableSeatsOnThisDate(connection, trainId, date);
                int availableSeatsNow = availableSeats - passengersData.get("passengerSurname").length;
                if (availableSeatsNow > 0) {
                    scheduleDAO.changeTrainAvailableSeatsOnThisDate(connection, trainId, date, availableSeatsNow);
                    connection.commit();
                    return "success.jsp";
                }
                connection.commit();
                session.setAttribute("errorMessage", "Вільних місць менше, ніж замовлених білетів, виберіть інший поїзд");
                return chooseLink(request);
            } catch (SQLException e) {
                logger.info("Failed to change train available seats on this date");
                DBManager.getInstance().rollback(connection, e);
            } finally {
                DBManager.getInstance().close(connection);
            }
        }
        session.setAttribute("errorMessage", "Будь ласка, авторизуйтеся");
        return chooseLink(request);
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
        } catch (NumberFormatException e) {
            logger.info("Train id is incorrect", e);
            request.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        List<String> departureDate = Arrays.asList(request.getParameter("departureDate").split("-"));
        if (departureDate.size() != 3) {
            logger.info("Departure date is incorrect");
            request.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        for (String d:
                departureDate) {
            try {
                Integer.parseInt(d);
            } catch (NumberFormatException e) {
                logger.info("Departure date is incorrect", e);
                request.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
                return false;
            }
        }
        date = String.join("-", departureDate);
        passengersData = request.getParameterMap();
        String[] passengerNames = passengersData.get("passengerName");
        String[] passengerSurnames = passengersData.get("passengerSurname");
        for (String passengerName:
             passengerNames) {
            if (passengerName.equals("")) {
                logger.info("Passenger name is incorrect");
                request.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
                return false;
            }
        }
        for (String passengerSurname:
                passengerSurnames) {
            if (passengerSurname.equals("")) {
                logger.info("Passenger surname is incorrect");
                request.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
                return false;
            }
        }
        return true;
    }

    private String chooseLink(HttpServletRequest request) {
        String link = "controller?command=mainPage";
        int fromStationId;
        int toStationId;
        List<String> formattedDate = Arrays.asList(date.split("-"));
        try {
            fromStationId = Integer.parseInt(request.getParameter("fromStationId"));
            toStationId = Integer.parseInt(request.getParameter("toStationId"));
            for (String d:
                    formattedDate) {
                Integer.parseInt(d);
            }
        } catch (NumberFormatException e) {
            return "controller?command=mainPage";
        }
        if (formattedDate.size() == 3) {
            Collections.reverse(formattedDate);
            link = "controller?command=getTrains&from=" + fromStationId + "&to=" + toStationId + "&datePicker=" + String.join(".", formattedDate);
        }
        return link;
    }
}
