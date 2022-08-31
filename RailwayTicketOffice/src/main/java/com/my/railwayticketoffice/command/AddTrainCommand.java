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
import java.util.Arrays;
import java.util.List;

/**
 * Class that built at command pattern. Add train to database.
 *
 * @author Yevhen Pashchenko
 */
public class AddTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private String trainNumber;
    private int trainSeats;
    private String trainDepartureTime;

    /**
     * Add train to database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole()) && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                trainDAO.addTrain(connection, trainNumber, trainSeats, trainDepartureTime);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("successMessage", "New train has been created");
                } else {
                    session.setAttribute("successMessage", "Новий поїзд створено");
                }
            } catch (SQLException e) {
                logger.info("Failed to add new train in database");
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("errorMessage", "Failed to connect to database for create new train in database");
                } else {
                    session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб створити новий поїзд в базі даних");
                }
                throw new DBException("Failed to connect to database for create new train in database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        trainNumber = request.getParameter("trainNumber");
        if ("".equals(trainNumber)) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Train number is not specified");
            } else {
                session.setAttribute("errorMessage", "Номер поїзда не задано");
            }
            return false;
        }
        try {
            trainSeats = Integer.parseInt(request.getParameter("trainSeats"));
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Train seats is not specified");
            } else {
                session.setAttribute("errorMessage", "Кількість місць в поїзді не задано");
            }
            return false;
        }
        trainDepartureTime = request.getParameter("trainDepartureTime");
        List<String> minutesSeconds = Arrays.asList(trainDepartureTime.split(":"));
        if (minutesSeconds.size() != 2) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Train departure time is not specified");
            } else {
                session.setAttribute("errorMessage", "Час відправлення поїзда не задано");
            }
            return false;
        }
        try {
            for (String time:
                 minutesSeconds) {
                Integer.parseInt(time);
            }
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Train departure time is not specified");
            } else {
                session.setAttribute("errorMessage", "Час відправлення поїзда не задано");
            }
            return false;
        }
        return true;
    }
}
