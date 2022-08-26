package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.StationDAO;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that built at command pattern. Edit station data in database.
 */
public class EditStationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditStationCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private int stationId;
    private String stationName;

    /**
     * Edit station data in database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole()) && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                stationDAO.editStation(connection, stationId, stationName);
                session.setAttribute("successMessage", "Дані станції відредаговано");
            } catch (SQLException e) {
                logger.warn("Failed to edit station data in database", e);
                throw new DBException("Failed to edit station data in database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        stationName = request.getParameter("stationName");
        try {
            stationId = Integer.parseInt(request.getParameter("stationId"));
        } catch (NumberFormatException e) {
            logger.info("Station id is incorrect");
            session.setAttribute("errorMessage", "Виберіть існуючу станцію для редагування");
            return false;
        }
        if ("".equals(stationName)) {
            logger.info("Station name is incorrect");
            session.setAttribute("errorMessage", "Ім'я станції не може бути пустим");
            return false;
        }
        return true;
    }
}
