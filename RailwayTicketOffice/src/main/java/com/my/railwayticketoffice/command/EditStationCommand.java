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
            if (stationService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    stationDAO.editStation(connection, Integer.parseInt(parameters.get("stationId")), parameters.get("stationName"), locale);
                    if ("en".equals(locale)) {
                        session.setAttribute("successMessage", "Station data has been edited");
                    } else {
                        session.setAttribute("successMessage", "Дані станції відредаговано");
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for edit station data in database", e);
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database for edit station data in database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб відредагувати станцію");
                    }
                    throw new DBException("Failed to connect to database for edit station data in database");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
