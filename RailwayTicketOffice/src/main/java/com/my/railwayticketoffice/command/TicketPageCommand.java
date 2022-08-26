package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class that built at command pattern. Prepare data for ticket.jsp page.
 *
 * @author Yevhen Pashchenko
 */
public class TicketPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(TicketPageCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;
    private int fromStationId;
    private int toStationId;
    private String formattedDate;

    /**
     * Prepare data for ticket.jsp page.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return - link to ticket.jsp or link to {@link MainPageCommand} if incoming data is incorrect.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                Train train = trainDAO.getTrainThatIsInSchedule(connection, trainId);
                trainDAO.getRouteForTrain(connection, train);
                request.setAttribute("train", train);
                request.setAttribute("fromStationId", fromStationId);
                request.setAttribute("toStationId", toStationId);
                request.setAttribute("departureDate", LocalDate.parse(formattedDate));
                return "ticket.jsp";
            } catch (SQLException e) {
                logger.warn("Failed to get train from database", e);
                throw new DBException("Failed to get train from database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
        } catch (NumberFormatException e) {
            logger.info("Train id is incorrect", e);
            session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        try {
            fromStationId = Integer.parseInt(request.getParameter("from"));
        } catch (NumberFormatException e) {
            logger.info("Departure station id is incorrect", e);
            session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        try {
            toStationId = Integer.parseInt(request.getParameter("to"));
        } catch (NumberFormatException e) {
            logger.info("Destination station id is incorrect", e);
            session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        if (fromStationId == toStationId) {
            session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        List<String> date = Arrays.asList(request.getParameter("datePicker").split("\\."));
        if (date.size() != 3) {
            logger.info("Departure date is incorrect");
            session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            return false;
        }
        for (String d:
                date) {
            try {
                Integer.parseInt(d);
            } catch (NumberFormatException e) {
                logger.info("Departure date is incorrect", e);
                session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
                return false;
            }
        }
        Collections.reverse(date);
        formattedDate = String.join("-", date);
        return true;
    }
}
