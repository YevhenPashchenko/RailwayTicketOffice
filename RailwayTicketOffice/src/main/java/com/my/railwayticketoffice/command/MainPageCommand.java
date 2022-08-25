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
import java.util.List;

/**
 * Class that built at command pattern. Prepare data for main.jsp page.
 *
 * @author Yevhen Pashchenko
 */
public class MainPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(MainPageCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();

    /**
     * Prepare data for main.jsp page.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to main.jsp
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Station> stations;
        try(Connection connection = DBManager.getInstance().getConnection()) {
            stations = stationDAO.getStations(connection);
            request.setAttribute("stations", stations);
            if (user != null && "admin".equals(user.getRole())) {
                List<Train> trains = trainDAO.getAllTrains(connection);
                request.setAttribute("trainsForAdmin", trains);
            }
        } catch (SQLException e) {
            logger.warn("Failed to get stations from database", e);
            throw new DBException("Failed to get stations from database");
        }
        return "main.jsp";
    }
}
