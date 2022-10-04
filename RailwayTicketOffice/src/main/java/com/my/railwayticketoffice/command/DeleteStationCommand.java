package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.StationParameterService;
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
 * Class that built at command pattern. Delete station from database.
 *
 * @author Yevhen Pashchenko
 */
public class DeleteStationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteStationCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> stationService = new StationParameterService();

    /**
     * Delete station from database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("stationId", request.getParameter("stationId"));
            parameters.put("stationName", request.getParameter("stationName"));
            if (stationService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isStationExists = stationDAO.checkIfStationExists(connection, parameters.get("stationName"), locale);
                    if (isStationExists == 0 || isStationExists != Integer.parseInt(parameters.get("stationId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "A station with this name no exists");
                        } else {
                            session.setAttribute("errorMessage", "Станції з такою назвою не існує");
                        }
                    } else {
                        if (scheduleDAO.checkIfTrainsThatHasThisStationOnTheRouteIsInSchedule(connection, Integer.parseInt(parameters.get("stationId")))) {
                            if ("en".equals(session.getAttribute("locale"))) {
                                session.setAttribute("errorMessage", "Cannot be deleted, trains with this station on the route is in the schedule");
                            } else {
                                session.setAttribute("errorMessage", "Неможливо видалити, поїзди з цією станцію на маршруті є в розкладі");
                            }
                        } else {
                            stationDAO.deleteStation(connection, Integer.parseInt(parameters.get("stationId")));
                            if ("en".equals(locale)) {
                                session.setAttribute("successMessage", "Station deleted");
                            } else {
                                session.setAttribute("successMessage", "Станцію видалено");
                            }
                        }
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to delete station from database", e);
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database for delete station from database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити станцію з бази даних");
                    }
                    throw new DBException("Failed to connect to database for delete station from database");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
