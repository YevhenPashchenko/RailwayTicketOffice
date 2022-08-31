package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that built at command pattern. Delete station from train route.
 *
 * @author Yevhen Pashchenko
 */
public class DeleteStationFromTrainRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteStationFromTrainRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;
    private int stationId;

    /**
     * Delete station from train route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link ShowRouteCommand} if deleted successfully otherwise link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole()) && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                trainDAO.deleteStationFromTrainRoute(connection, trainId, stationId);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("successMessage", "Station is deleted from train route");
                } else {
                    session.setAttribute("successMessage", "Станцію видалено з маршруту поїзда");
                }
                return "controller?command=showRoute&trainId=" + trainId;
            } catch (SQLException e) {
                logger.warn("Failed to connect to database for delete station from train route", e);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database for delete station from train route");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити станцію з маршруту поїзда");
                }
                throw new DBException("Failed to connect to database for delete station from train route");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
            stationId = Integer.parseInt(request.getParameter("stationId"));
        } catch (NumberFormatException e) {
            logger.info("Link data is incorrect", e);
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Request error, try again");
            } else {
                session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            }
            return false;
        }
        return true;
    }
}
