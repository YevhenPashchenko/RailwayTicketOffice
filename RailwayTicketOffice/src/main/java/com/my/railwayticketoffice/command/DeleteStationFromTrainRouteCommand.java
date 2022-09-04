package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.StationParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
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
 * Class that built at command pattern. Delete station from train route.
 *
 * @author Yevhen Pashchenko
 */
public class DeleteStationFromTrainRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteStationFromTrainRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> stationService = new StationParameterService();

    /**
     * Delete station from train route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link ShowRouteCommand} if deleted successfully otherwise link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("trainId", request.getParameter("trainId"));
            parameters.put("stationId", request.getParameter("stationId"));
            if (trainService.check(parameters, session) && stationService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    trainDAO.deleteStationFromTrainRoute(connection, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("stationId")));
                    return "controller?command=showRoute&trainId=" + Integer.parseInt(parameters.get("trainId"));
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for delete station from train route", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for delete station from train route");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити станцію з маршруту поїзда");
                    }
                    throw new DBException("Failed to connect to database for delete station from train route");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
