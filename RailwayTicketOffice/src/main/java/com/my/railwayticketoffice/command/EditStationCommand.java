package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
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
 * Class that built at command pattern. Edit station data in database.
 */
public class EditStationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditStationCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private final ParameterService<String> stationService = new StationParameterService();

    /**
     * Edit station data in database.
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
            parameters.put("stationNameUA", request.getParameter("stationNameUA"));
            parameters.put("stationNameEN", request.getParameter("stationNameEN"));
            if (stationService.check(parameters, session)) {
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    int isStationExist = stationDAO.checkIfStationExists(connection, parameters.get("stationName"), locale);
                    int isNewStationUAExist = stationDAO.checkIfStationExists(connection, parameters.get("stationNameUA"), "uk");
                    int isNewStationENExist = stationDAO.checkIfStationExists(connection, parameters.get("stationNameEN"), "en");
                    if (isStationExist == 0 || isStationExist != Integer.parseInt(parameters.get("stationId"))) {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "A station with this name no exists");
                        } else {
                            session.setAttribute("errorMessage", "Станції з такою назвою не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    if (isNewStationUAExist > 0 || isNewStationENExist > 0) {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "A station with this name already exists");
                        } else {
                            session.setAttribute("errorMessage", "Станція з таким ім'ям вже існує");
                        }
                        return "controller?command=mainPage";
                    }
                    connection.setAutoCommit(false);
                    stationDAO.editStation(connection, Integer.parseInt(parameters.get("stationId")), parameters.get("stationNameUA"), "uk");
                    stationDAO.editStation(connection, Integer.parseInt(parameters.get("stationId")), parameters.get("stationNameEN"), "en");
                    connection.setAutoCommit(true);
                    if ("en".equals(locale)) {
                        session.setAttribute("successMessage", "Station data has been edited");
                    } else {
                        session.setAttribute("successMessage", "Дані станції відредаговано");
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for edit station data in database", e);
                    DBManager.getInstance().rollback(session, connection, e);
                } finally {
                    DBManager.getInstance().close(connection);
                }
            }
        }
        return "controller?command=mainPage";
    }
}
