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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class that built at command pattern. Get from database map in which key is {@link com.my.railwayticketoffice.entity.Train} id
 * and value is {@link com.my.railwayticketoffice.entity.Train} traveling through the station specified in the request in the date specified in the request.
 *
 * @author Yevhen Pashchenko
 */
public class GetTrainsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GetTrainsCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int fromStationId;
    private int toStationId;
    String formattedDate;

    /**
     * Get from database map in which key is {@link com.my.railwayticketoffice.entity.Train} id
     * and value is {@link com.my.railwayticketoffice.entity.Train} traveling through the station specified in the request in the date specified in the request.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return request to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<Integer, Train> trains;
        if (checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                trains = trainDAO.getTrainsSpecifiedByStationsAndDate(connection, fromStationId, toStationId, formattedDate);
                if (trains.size() > 0) {
                    trainDAO.getRoutesForTrains(connection, trains);
                }
                request.setAttribute("trains", trains);
            } catch (SQLException e) {
                logger.warn("Failed to get trains specified by stations and date", e);
                throw new DBException("Failed to get trains specified by stations and date");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        try {
            fromStationId = Integer.parseInt(request.getParameter("from"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Пункт відправлення задано не коректно");
            return false;
        }
        try {
            toStationId = Integer.parseInt(request.getParameter("to"));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Пункт призначення задано не коректно");
            return false;
        }
        if (fromStationId == toStationId) {
            request.setAttribute("errorMessage", "Станції відправлення та призначення співпадають");
            return false;
        }
        List<String> date = Arrays.asList(request.getParameter("datePicker").split("\\."));
        if (date.size() != 3) {
            request.setAttribute("errorMessage", "Дату задано не коректно");
            return false;
        }
        for (String d:
             date) {
            try {
                Integer.parseInt(d);
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Дату задано не коректно");
                return false;
            }
        }
        Collections.reverse(date);
        formattedDate = String.join("-", date);
        return true;
    }
}
