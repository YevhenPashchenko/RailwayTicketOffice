package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Class that built at command pattern. Get from database {@link com.my.railwayticketoffice.entity.Train} and his route.
 *
 * @author Yevhen Pashchenko
 */
public class ShowRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ShowRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private int trainId;
    private int fromStationId;
    private int toStationId;

    /**
     * Get from database {@link com.my.railwayticketoffice.entity.Train} and his route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to route.jsp.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("locale");
        User user = (User) session.getAttribute("user");
        if (checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                Train train = trainDAO.getTrainThatIsInSchedule(connection, trainId);
                if (user != null && "admin".equals(user.getRole())) {
                    if (train.getId() == 0) {
                        train = trainDAO.getTrain(connection, trainId);
                    }
                    List<Station> stations = stationDAO.getStations(connection, locale);
                    request.setAttribute("stations", stations);
                }
                trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), locale);
                request.setAttribute("fromStationId", fromStationId);
                request.setAttribute("toStationId", toStationId);
                request.setAttribute("train", train);
            } catch (SQLException e) {
                logger.warn("Failed to connect to database for get train route", e);
                if ("en".equals(locale)) {
                    session.setAttribute("errorMessage", "Failed to connect to database for get train route");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати маршрут поїзда");
                }
                throw new DBException("Failed to connect to database for get train route");
            }
        } else {
            return "controller?command=mainPage";
        }
        return "route.jsp";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
            if (user != null && "admin".equals(user.getRole())) {
                if (request.getParameter("fromStationId") != null) {
                    fromStationId = Integer.parseInt(request.getParameter("fromStationId"));
                } else {
                    fromStationId = 0;
                }
                if (request.getParameter("toStationId") != null) {
                    toStationId = Integer.parseInt(request.getParameter("toStationId"));
                } else {
                    toStationId = 0;
                }
            } else {
                fromStationId = Integer.parseInt(request.getParameter("fromStationId"));
                toStationId = Integer.parseInt(request.getParameter("toStationId"));
            }
        } catch (NumberFormatException e) {
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
