package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that built at command pattern. Edit station data on train route.
 *
 * @author Yevhen Pashchenko
 */
public class EditStationDataOnTrainRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditStationDataOnTrainRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;
    private int stationId;
    private String timeSinceStart;
    private String stopTime;
    private int distanceFromStart;

    /**
     * Edit station data on train route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link ShowRouteCommand} if edited successfully otherwise link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole()) && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                trainDAO.editStationDataOnTrainRoute(connection, timeSinceStart, stopTime, distanceFromStart, trainId, stationId);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("successMessage", "Station data on train route has been edited");
                } else {
                    session.setAttribute("successMessage", "Дані станції на маршруті поїзда відредаговано");
                }
                return "controller?command=showRoute&trainId=" + trainId;
            } catch (SQLException e) {
                logger.warn("Failed to connect to database for edit station data on train route in database", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database for edit station data on train route in database");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб відредагувати станцію на маршруті поїзда");
                }
                throw new DBException("Failed to connect to database for edit station data on train route in database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        timeSinceStart = request.getParameter("timeSinceStart");
        if ("".equals(timeSinceStart)) {
            logger.info("Time since start is incorrect");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Time since the departure of the train from the first station of the route is not specified");
            } else {
                session.setAttribute("errorMessage", "Час з моменту відправлення поїзда з першої станції маршруту не задано");
            }
            return false;
        }
        stopTime = request.getParameter("stopTime");
        if ("".equals(stopTime)) {
            logger.info("Stop time is incorrect");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Time stop of the train at the station is not specified");
            } else {
                session.setAttribute("errorMessage", "Час зупинки поїзда на станції не задано");
            }
            return false;
        }
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
            stationId = Integer.parseInt(request.getParameter("stationId"));
            distanceFromStart = Integer.parseInt(request.getParameter("distanceFromStart"));
            for (String time:
                    timeSinceStart.split(":")) {
                Integer.parseInt(time);
            }
            for (String time:
                    stopTime.split(":")) {
                Integer.parseInt(time);
            }
        } catch (NumberFormatException e) {
            logger.info("Link data is incorrect", e);
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Request error, try again");
            } else {
                session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            }
            return false;
        }
        return true;
    }
}
