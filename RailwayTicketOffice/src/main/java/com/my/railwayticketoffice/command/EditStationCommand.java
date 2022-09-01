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
        String locale = (String) session.getAttribute("locale");
        if (user != null && "admin".equals(user.getRole()) && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                stationDAO.editStation(connection, stationId, stationName, locale);
                if ("en".equals(locale)) {
                    session.setAttribute("successMessage", "Station data has been edited");
                } else {
                    session.setAttribute("successMessage", "Дані станції відредаговано");
                }
                session.setAttribute("successMessage", "Дані станції відредаговано");
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
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        stationName = request.getParameter("stationName");
        try {
            stationId = Integer.parseInt(request.getParameter("stationId"));
        } catch (NumberFormatException e) {
            logger.info("Station id is incorrect");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Request error, try again");
            } else {
                session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            }
            return false;
        }
        if (stationName == null || "".equals(stationName)) {
            logger.info("Station name is incorrect");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Station name cannot be empty");
            } else {
                session.setAttribute("errorMessage", "Ім'я станції не може бути пустим");
            }
            return false;
        }
        return true;
    }
}
