package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Station;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.SearchTrainParameterService;
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
import java.util.List;
import java.util.Map;

/**
 * Class that built at command pattern. Get from database {@link com.my.railwayticketoffice.entity.Train} and his route.
 *
 * @author Yevhen Pashchenko
 */
public class ShowRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ShowRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> searchTrainService = new SearchTrainParameterService();

    /**
     * Get from database {@link com.my.railwayticketoffice.entity.Train} and his route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to route.jsp.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("locale");
        User user = (User) session.getAttribute("user");
        parameters.put("trainId", request.getParameter("trainId"));
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        if (trainService.check(parameters, session)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                Train train = trainDAO.getTrainThatIsInSchedule(connection, Integer.parseInt(parameters.get("trainId")));
                if (user != null && "admin".equals(user.getRole())) {
                    if (train.getId() == 0) {
                        train = trainDAO.getTrain(connection, Integer.parseInt(parameters.get("trainId")));
                    }
                    List<Station> stations = stationDAO.getStations(connection, locale);
                    request.setAttribute("stations", stations);
                }
                trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), locale);
                if (searchTrainService.check(parameters, session)) {
                    request.setAttribute("from", Integer.parseInt(parameters.get("from")));
                    request.setAttribute("to", Integer.parseInt(parameters.get("to")));
                }
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
}
