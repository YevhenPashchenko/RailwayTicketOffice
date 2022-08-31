package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.filter.TrainFilter;
import com.my.railwayticketoffice.filter.TrainFilterByDirectionAndDepartureTime;
import com.my.railwayticketoffice.pagination.MainPagePagination;
import com.my.railwayticketoffice.pagination.Pagination;
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
 * Class that built at command pattern. Get from database list of {@link com.my.railwayticketoffice.entity.Train}
 * traveling through the station specified in the request in the date specified in the request and sorted them by available seats.
 */
public class GetTrainsSortedByAvailableSeatsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GetTrainsSortedByAvailableSeatsCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final TrainFilter trainFilter = new TrainFilterByDirectionAndDepartureTime();
    private final Pagination pagination = new MainPagePagination();
    private int page;
    private int fromStationId;
    private int toStationId;
    private String formattedDate;

    /**
     * Get from database list of {@link com.my.railwayticketoffice.entity.Train}
     * traveling through the station specified in the request in the date specified in the request and sorted them by available seats.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("locale");
        if (checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                List<Train> trains = trainDAO.getTrainsSpecifiedByStationsAndDate(connection, fromStationId, toStationId, formattedDate);
                if (trains.size() > 0) {
                    trainDAO.getRoutesForTrains(connection, trains, locale);
                    List<Train> filteredTrains = trainFilter.filter(trains, fromStationId, toStationId, LocalDate.parse(formattedDate));
                    int numberOfPages = (int) Math.ceil((float) filteredTrains.size() / Util.getNumberTrainOnPage());
                    List<Train> trainsPerPage = pagination.paginate(filteredTrains, page);
                    request.setAttribute("trains", trainsPerPage);
                    request.setAttribute("numberOfPages", numberOfPages);
                    request.setAttribute("departureDateForSortingAndPagination", request.getParameter("datePicker"));
                    request.setAttribute("trainsSortedCommand", "getTrainsSortedByAvailableSeats");
                }
                request.setAttribute("page", page);
                request.setAttribute("fromStationId", fromStationId);
                request.setAttribute("toStationId", toStationId);
                request.setAttribute("departureDate", LocalDate.parse(formattedDate));
            } catch (SQLException e) {
                logger.warn("Failed to connect to database for get trains specified by stations and date", e);
                if ("en".equals(locale)) {
                    session.setAttribute("errorMessage", "Failed to connect to database for get trains specified by stations and date");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати поїзди за заданими станціями та датою");
                }
                throw new DBException("Failed to connect to database for get trains specified by stations and date");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
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
