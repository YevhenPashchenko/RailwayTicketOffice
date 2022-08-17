package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that built at command pattern. Get from database {@link com.my.railwayticketoffice.entity.Train} and his route.
 *
 * @author Yevhen Pashchenko
 */
public class ShowRouteCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ShowRouteCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;
    private int fromStationId;
    private int toStationId;

    /**
     * Get from database {@link com.my.railwayticketoffice.entity.Train} and his route.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to route.jsp.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        if (checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                Train train = trainDAO.getTrain(connection, trainId);
                trainDAO.getRouteForTrain(connection, train);
                request.setAttribute("fromStationId", fromStationId);
                request.setAttribute("toStationId", toStationId);
                request.setAttribute("train", train);
            } catch (SQLException e) {
                logger.warn("Failed to get route", e);
                throw new DBException("Failed to get route");
            }
        } else {
            return "controller?command=mainPage";
        }
        return "route.jsp";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
            fromStationId = Integer.parseInt(request.getParameter("fromStationId"));
            toStationId = Integer.parseInt(request.getParameter("toStationId"));
        } catch (NumberFormatException e) {
            logger.info("Link data is incorrect", e);
            request.setAttribute("errorMessage", "Не вдалося побудувати маршрут так як в ссилці передано не коректні параметри");
            return false;
        }
        return true;
    }
}
