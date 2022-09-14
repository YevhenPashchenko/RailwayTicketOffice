package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.filter.TrainFilter;
import com.my.railwayticketoffice.filter.TrainFilterByCarriagesFreeSeats;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TicketParameterService;
import com.my.railwayticketoffice.service.TrainSearchParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Class that built at command pattern. Prepare data for seats.jsp page.
 */
public class ChooseSeatsPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(ChooseSeatsPageCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ParameterService<String> searchTrainService = new TrainSearchParameterService();
    private final ParameterService<String[]> ticketParameterService = new TicketParameterService();
    private final TrainFilter trainFilter = new TrainFilterByCarriagesFreeSeats();

    /**
     * Prepare data for seats.jsp page.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @return link to seats.jsp or {@link MainPageCommand} if parameters incorrect.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        Map<String, String[]> ticketParameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String locale = (String) session.getAttribute("locale");
        if (user != null) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                parameters.put("trainId", request.getParameter("trainId"));
                parameters.put("from", request.getParameter("from"));
                parameters.put("to", request.getParameter("to"));
                parameters.put("date", request.getParameter("departureDate"));
                parameters.put("carriageType", request.getParameter("carriageType"));
                if (request.getParameterMap().get("carriage") != null) {
                    ticketParameters.put("carriage", request.getParameterValues("carriage"));
                    ticketParameters.put("seat", request.getParameterValues("seat"));
                    ticketParameters.put("cost", request.getParameterValues("cost"));
                }
                if (trainService.check(parameters, session) && searchTrainService.check(parameters, session) && ticketParameterService.check(ticketParameters, session)) {
                    Train train = trainDAO.getTrain(connection, Integer.parseInt(parameters.get("trainId")));
                    trainDAO.getRoutesForTrains(connection, Collections.singletonList(train), locale);
                    trainDAO.getCarriagesForTrains(connection, Collections.singletonList(train));
                    List<String> dateForDB = Arrays.asList(parameters.get("date").split("\\."));
                    Collections.reverse(dateForDB);
                    trainDAO.getFreeSeatsForTrainsByDate(connection, Collections.singletonList(train), String.join("-", dateForDB));
                    trainFilter.filter(Collections.singletonList(train), parameters);
                    int freeSeatsSumByChosenCarriageType = 0;
                    for (int i = 1; i <= train.getCarriagesTypesNumber(); i++) {
                        if (parameters.get("carriageType").equals(train.getCarriageTypeOrderByMaxSeats(i))) {
                            freeSeatsSumByChosenCarriageType = train.getFreeSeatsSumByCarriageType(i);
                        }
                    }
                    if (freeSeatsSumByChosenCarriageType > 0) {
                        if (request.getParameter("carriageNumber") == null) {
                            int carriageNumber = train.getCarriagesFilteredByTypeAndSortedByNumber(parameters.get("carriageType"))
                                    .values().iterator().next().getNumber();
                            request.setAttribute("carriageNumber", carriageNumber);
                        } else {
                            request.setAttribute("carriageNumber", request.getParameter("carriageNumber"));
                        }
                        request.setAttribute("train", train);
                        request.setAttribute("from", parameters.get("from"));
                        request.setAttribute("to", parameters.get("to"));
                        request.setAttribute("departureDate", parameters.get("date"));
                        request.setAttribute("carriageType", parameters.get("carriageType"));
                        if (request.getParameterMap().get("carriage") != null) {
                            request.setAttribute("carriage", ticketParameters.get("carriage"));
                            request.setAttribute("seat", ticketParameters.get("seat"));
                            request.setAttribute("cost", ticketParameters.get("cost"));
                        }
                        return "seats.jsp";
                    } else {
                        if ("en".equals(locale)) {
                            session.setAttribute("errorMessage", "There are no free seats in carriages of this type");
                        } else {
                            session.setAttribute("errorMessage", "Не знайдено вільних місць в вагонах цього типу");
                        }
                    }
                }
            } catch (SQLException e) {
                logger.warn("Failed to connect to database to get free train seats from database", e);
                if ("en".equals(locale)) {
                    session.setAttribute("errorMessage", "Failed to connect to database to get free train seats from database");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб отримати вільні місця в поїзді");
                }
                throw new DBException("Failed to connect to database to get free train seats from database");
            }
        }
        return "controller?command=mainPage";
    }
}
