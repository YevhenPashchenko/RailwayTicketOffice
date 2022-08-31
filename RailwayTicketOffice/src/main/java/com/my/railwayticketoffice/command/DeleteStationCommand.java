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
 * Class that built at command pattern. Delete station from database.
 *
 * @author Yevhen Pashchenko
 */
public class DeleteStationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteStationCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private int stationId;

    /**
     * Delete station from database.
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
                stationDAO.deleteStation(connection, stationId);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("successMessage", "Station deleted");
                } else {
                    session.setAttribute("successMessage", "Станцію видалено");
                }
            } catch (SQLException e) {
                logger.warn("Failed to delete station from database", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database for delete station from database");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити станцію з бази даних");
                }
                throw new DBException("Failed to connect to database for delete station from database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            stationId = Integer.parseInt(request.getParameter("stationId"));
        } catch (NumberFormatException e) {
            logger.info("Station id is incorrect");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Select an existing station");
            } else {
                session.setAttribute("errorMessage", "Виберіть існуючу станцію");
            }
            return false;
        }
        return true;
    }
}
