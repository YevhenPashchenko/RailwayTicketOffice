package com.my.railwayticketoffice.command;


import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.entity.Station;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Class that built at command pattern. Prepare data for main.jsp page.
 *
 * @author Yevhen Pashchenko
 */
public class MainPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(MainPageCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();

    /**
     * Prepare data for main.jsp page.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to main.jsp
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        List<Station> stations;
        try(Connection connection = DBManager.getInstance().getConnection()) {
            stations = stationDAO.getStations(connection);
            request.setAttribute("stations", stations);
        } catch (SQLException e) {
            logger.warn("Failed to get stations from database", e);
            throw new DBException("Failed to get stations from database");
        }
        return "main.jsp";
    }
}
