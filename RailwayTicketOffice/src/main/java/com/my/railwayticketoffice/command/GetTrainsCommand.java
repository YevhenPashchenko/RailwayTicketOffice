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

/**
 * Class that built at command pattern. Get from database list of {@link com.my.railwayticketoffice.entity.Train}
 * traveling through the station specified in the request in the date specified in the request.
 *
 * @author Yevhen Pashchenko
 */
public class GetTrainsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GetTrainsCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();

    /**
     * Get from database list of {@link com.my.railwayticketoffice.entity.Train}
     * traveling through the station specified in the request in the date specified in the request.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return request to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        int fromStationId = Integer.parseInt(request.getParameter("from"));
        int toStationId = Integer.parseInt(request.getParameter("to"));
        List<String> date = Arrays.asList(request.getParameter("datePicker").split("\\."));
        Collections.reverse(date);
        String formattedDate = String.join("-", date);
        List<Train> trains;
        try(Connection connection = DBManager.getInstance().getConnection()) {
            trains = trainDAO.getTrainsSpecifiedByStationsAndDate(connection, fromStationId, toStationId, formattedDate);
        } catch (SQLException e) {

        }
        return "controller?command=mainPage";
    }
}
