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
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.SearchTrainParameterService;
import com.my.railwayticketoffice.sorting.TrainSorting;
import com.my.railwayticketoffice.sorting.TrainSortingManger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Class that built at command pattern. Get from database list of {@link com.my.railwayticketoffice.entity.Train}
 * traveling through the station specified in the request in the date specified in the request.
 *
 * @author Yevhen Pashchenko
 */
public class GetTrainsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(GetTrainsCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final TrainFilter trainFilter = new TrainFilterByDirectionAndDepartureTime();
    private final Pagination pagination = new MainPagePagination();
    private final ParameterService<String> searchTrainService = new SearchTrainParameterService();

    /**
     * Get from database list of {@link com.my.railwayticketoffice.entity.Train}
     * traveling through the station specified in the request in the date specified in the request.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        String locale = (String) session.getAttribute("locale");
        parameters.put("page", request.getParameter("page"));
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (searchTrainService.check(parameters, session)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                List<String> dateForDB = Arrays.asList(parameters.get("date").split("\\."));
                Collections.reverse(dateForDB);
                List<Train> trains = trainDAO.getTrainsSpecifiedByStationsAndDate(connection, Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), String.join("-", dateForDB));
                if (trains.size() > 0) {
                    trainDAO.getRoutesForTrains(connection, trains, locale);
                    List<Train> filteredTrains = trainFilter.filter(trains, Integer.parseInt(parameters.get("from")), Integer.parseInt(parameters.get("to")), LocalDate.parse(String.join("-", dateForDB)));
                    if (request.getParameter("sort") != null) {
                        session.setAttribute("sort", request.getParameter("sort"));
                    }
                    List<Train> sortedTrains;
                    if (session.getAttribute("sort") != null) {
                        TrainSorting trainSorting = TrainSortingManger.getStrategy((String) session.getAttribute("sort"));
                        sortedTrains = trainSorting.sort(filteredTrains, parameters);
                    } else {
                        sortedTrains = filteredTrains;
                    }
                    int numberOfPages = (int) Math.ceil((float) sortedTrains.size() / Util.getNumberTrainOnPage());
                    List<Train> trainsPerPage = pagination.paginate(sortedTrains, Integer.parseInt(parameters.get("page")));
                    request.setAttribute("trains", trainsPerPage);
                    request.setAttribute("numberOfPages", numberOfPages);
                }
                request.setAttribute("page", Integer.parseInt(parameters.get("page")));
                request.setAttribute("from", Integer.parseInt(parameters.get("from")));
                request.setAttribute("to", Integer.parseInt(parameters.get("to")));
                request.setAttribute("departureDate", parameters.get("date"));
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
}
