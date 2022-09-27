package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.filter.EditStationDataOnTrainRouteFilter;
import com.my.railwayticketoffice.filter.TrainFilter;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.RouteParameterService;
import com.my.railwayticketoffice.service.StationParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Add station to train route.
 *
 * @author Yevhen Pashchenko
 */
public class AddStationToTrainRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddStationToTrainRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> stationService = new StationParameterService();
    private final ParameterService<String> routeService = new RouteParameterService();
    private final TrainFilter addStationDataOnTrainRouteFilter = new EditStationDataOnTrainRouteFilter();

    /**
     * Add station to train route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link ShowRouteCommand} if added successfully otherwise link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("trainId", request.getParameter("trainId"));
            parameters.put("stationId", request.getParameter("stationId"));
            parameters.put("timeSinceStart", request.getParameter("timeSinceStart"));
            parameters.put("stopTime", request.getParameter("stopTime"));
            parameters.put("distanceFromStart", request.getParameter("distanceFromStart"));
            if (trainService.check(parameters, session) && stationService.check(parameters, session) && routeService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int trainId = Integer.parseInt(parameters.get("trainId"));
                    Train train = trainDAO.getTrain(connection, trainId);
                    if (trainId != train.getId()) {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "Train not found");
                        } else {
                            session.setAttribute("errorMessage", "Поїзд не знайдено");
                        }
                        return "controller?command=showRoute&trainId=" + trainId;
                    }
                    trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), locale);
                    Station station = new Station();
                    station.setId(Integer.parseInt(parameters.get("stationId")));
                    train.getRoute().addStationByTimeSinceStart(station, parameters.get("timeSinceStart"));
                    if (addStationDataOnTrainRouteFilter.filter(Collections.singletonList(train), parameters).size() > 0) {
                        trainDAO.addStationToTrainRoute(connection, parameters.get("timeSinceStart"), parameters.get("stopTime"), Integer.parseInt(parameters.get("distanceFromStart")), trainId, Integer.parseInt(parameters.get("stationId")));
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Station  has been added to the route");
                        } else {
                            session.setAttribute("successMessage", "Станцію додано до маршруту");
                        }
                    } else {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "Parameters for adding station to the route are incorrect");
                        } else {
                            session.setAttribute("errorMessage", "Параметри для додавання станції до маршруту не коректні");
                        }
                    }
                    return "controller?command=showRoute&trainId=" + trainId;
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for add station to train route", e);
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database for add station to train route");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб додати станцію до маршруту поїзда");
                    }
                    throw new DBException("Failed to connect to database for add station to train route");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
