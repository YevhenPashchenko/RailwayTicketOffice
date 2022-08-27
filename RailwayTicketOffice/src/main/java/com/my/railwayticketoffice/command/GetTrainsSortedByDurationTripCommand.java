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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that built at command pattern. Get from database list of {@link com.my.railwayticketoffice.entity.Train}
 * traveling through the station specified in the request in the date specified in the request and sorted them by duration trip.
 *
 * @author Yevhen Pashchenko
 */
public class GetTrainsSortedByDurationTripCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GetTrainsSortedByDurationTripCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final TrainFilter trainFilter = new TrainFilterByDirectionAndDepartureTime();
    private final Pagination pagination = new MainPagePagination();
    private int page;
    private int fromStationId;
    private int toStationId;
    private String formattedDate;

    /**
     * Get from database list of {@link com.my.railwayticketoffice.entity.Train}
     * traveling through the station specified in the request in the date specified in the request and sorted them by duration trip.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        if (checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                List<Train> trains = trainDAO.getTrainsSpecifiedByStationsAndDate(connection, fromStationId, toStationId, formattedDate);
                if (trains.size() > 0) {
                    trainDAO.getRoutesForTrains(connection, trains);
                    List<Train> filteredTrains = trainFilter.filter(trains, fromStationId, toStationId, LocalDate.parse(formattedDate)).stream()
                            .sorted((train1, train2) -> {
                                LocalTime durationTrip1 = LocalTime.parse(train1.getRoute().getDurationTrip(fromStationId, toStationId));
                                LocalTime durationTrip2 = LocalTime.parse(train2.getRoute().getDurationTrip(fromStationId, toStationId));
                                return durationTrip1.compareTo(durationTrip2);
                            })
                            .collect(Collectors.toList());
                    int numberOfPages = (int) Math.ceil((float) filteredTrains.size() / Util.getNumberTrainOnPage());
                    List<Train> trainsPerPage = pagination.paginate(filteredTrains, page);
                    request.setAttribute("trains", trainsPerPage);
                    request.setAttribute("numberOfPages", numberOfPages);
                    request.setAttribute("departureDateForSortingAndPagination", request.getParameter("datePicker"));
                    request.setAttribute("trainsSortedCommand", "getTrainsSortedByDurationTrip");
                }
                request.setAttribute("page", page);
                request.setAttribute("fromStationId", fromStationId);
                request.setAttribute("toStationId", toStationId);
                request.setAttribute("departureDate", LocalDate.parse(formattedDate));
            } catch (SQLException e) {
                logger.warn("Failed to get trains specified by stations and date", e);
                throw new DBException("Failed to get trains specified by stations and date");
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
        try {
            fromStationId = Integer.parseInt(request.getParameter("from"));
        } catch (NumberFormatException e) {
            logger.info("Departure station id is incorrect", e);
            session.setAttribute("errorMessage", "Пункт відправлення задано не коректно");
            return false;
        }
        try {
            toStationId = Integer.parseInt(request.getParameter("to"));
        } catch (NumberFormatException e) {
            logger.info("Destination station id is incorrect", e);
            session.setAttribute("errorMessage", "Пункт призначення задано не коректно");
            return false;
        }
        if (fromStationId == toStationId) {
            session.setAttribute("errorMessage", "Станції відправлення та призначення співпадають");
            return false;
        }
        List<String> date = Arrays.asList(request.getParameter("datePicker").split("\\."));
        if (date.size() != 3) {
            session.setAttribute("errorMessage", "Дату задано не коректно");
            return false;
        }
        for (String d:
                date) {
            try {
                Integer.parseInt(d);
            } catch (NumberFormatException e) {
                logger.info("Departure date is incorrect", e);
                session.setAttribute("errorMessage", "Дату задано не коректно");
                return false;
            }
        }
        Collections.reverse(date);
        formattedDate = String.join("-", date);
        return true;
    }
}
