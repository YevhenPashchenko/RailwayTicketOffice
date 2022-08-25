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
 * Class that built at command pattern. Add station to database.
 *
 * @author Yevhen Pashchenko
 */
public class AddStationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddStationCommand.class);
    private final StationDAO stationDAO = DBManager.getInstance().getStationDAO();
    private String stationName;

    /**
     * Add station to database.
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
                stationDAO.addStation(connection, stationName);
                session.setAttribute("successMessage", "Нову станцію додано");
            } catch (SQLException e) {
                logger.warn("Failed to add station to database", e);
                throw new DBException("Failed to add station to database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        stationName = request.getParameter("stationName");
        if ("".equals(stationName)) {
            logger.info("Station name is incorrect");
            session.setAttribute("errorMessage", "Ім'я станції не може бути пустим");
            return false;
        }
        return true;
    }
}
