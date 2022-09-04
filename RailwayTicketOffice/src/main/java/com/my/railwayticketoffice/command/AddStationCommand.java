package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.StationParameterService;
import com.my.railwayticketoffice.service.ParameterService;
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
 * Class that built at command pattern. Add station to database.
 *
 * @author Yevhen Pashchenko
 */
public class AddStationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddStationCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private final ParameterService<String> stationService = new StationParameterService();

    /**
     * Add station to database.
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
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("stationNameUA", request.getParameter("stationNameUA"));
            parameters.put("stationNameEN", request.getParameter("stationNameEN"));
            if (stationService.check(parameters, session)) {
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    connection.setAutoCommit(false);
                    int id = stationDAO.addStation(connection, parameters.get("stationNameUA"));
                    stationDAO.addStationEN(connection, id, parameters.get("stationNameEN"));
                    connection.commit();
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "New station has been added");
                    } else {
                        session.setAttribute("successMessage", "Нову станцію додано");
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for add station to database", e);
                    DBManager.getInstance().rollback(session, connection, e);
                } finally {
                    DBManager.getInstance().close(connection);
                }
            }
        }
        return "controller?command=mainPage";
    }
}
