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
import java.util.Map;

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
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @return link to main.jsp.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        List<Station> stations;
        try(Connection connection = DBManager.getInstance().getConnection()) {
            stations = stationDAO.getStations(connection, locale);
            request.setAttribute("stations", stations);
            if (user != null && "admin".equals(user.getRole())) {
                try {
                    List<Train> trains = trainDAO.getAllTrains(connection);
                    if (trains.size() > 0) {
                        trainDAO.getCarriagesForTrains(connection, trains);
                        Map<Integer, String> carriagesTypes = trainDAO.getCarriagesTypes(connection);
                        request.setAttribute("trainsForAdmin", trains);
                        request.setAttribute("carriagesTypes", carriagesTypes);
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database to get trains from database", e);
                    if ("en".equals(locale)) {
                        session.setAttribute("errorMessage", "Failed to connect to database to get trains from database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати поїзди");
                    }
                    throw new DBException("Failed to connect to database to get trains from database");
                }
            }
        } catch (SQLException e) {
            logger.warn("Failed to connect to database to get stations from database", e);
            if ("en".equals(locale)) {
                session.setAttribute("errorMessage", "Failed to connect to database to get stations from database");
            } else {
                session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати станції");
            }
            throw new DBException("Failed to connect to database to get stations from database");
        }
        return "main.jsp";
    }
}
