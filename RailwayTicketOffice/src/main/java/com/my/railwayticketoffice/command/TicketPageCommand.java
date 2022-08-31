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
        String locale = (String) session.getAttribute("locale");
        if (user != null && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                Train train = trainDAO.getTrainThatIsInSchedule(connection, trainId);
                trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), locale);
                request.setAttribute("train", train);
                request.setAttribute("fromStationId", fromStationId);
                request.setAttribute("toStationId", toStationId);
                request.setAttribute("departureDate", LocalDate.parse(formattedDate));
                request.setAttribute("unformattedDepartureDate", request.getParameter("datePicker"));
                return "ticket.jsp";
            } catch (SQLException e) {
                logger.warn("Failed to connect to database for get train and its route", e);
                if ("en".equals(locale)) {
                    session.setAttribute("errorMessage", "Failed to connect to database for get train and its route");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати поїзд та його маршрут");
                }
                throw new DBException("Failed to connect to database for get train and its route");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<String> date = Arrays.asList(request.getParameter("datePicker").split("\\."));
        if (date.size() != 3) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Departure date is incorrect");
            } else {
                session.setAttribute("errorMessage", "Дату відправлення задано не коректно");
            }
            return false;
        }
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
            fromStationId = Integer.parseInt(request.getParameter("from"));
            toStationId = Integer.parseInt(request.getParameter("to"));
            for (String d:
                    date) {
                Integer.parseInt(d);
            }
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Request error, try again");
            } else {
                session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            }
            return false;
        }
        if (fromStationId == toStationId) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Departure and destination stations match");
            } else {
                session.setAttribute("errorMessage", "Станції відправлення та призначення співпадають");
            }
            return false;
        }
        Collections.reverse(date);
        formattedDate = String.join("-", date);
        return true;
    }
}
