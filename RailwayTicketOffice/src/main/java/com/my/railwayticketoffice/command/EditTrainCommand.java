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
import java.time.LocalTime;

/**
 * Class that built at command pattern. Edit train data in database.
 *
 * @author Yevhen Pashchenko
 */
public class EditTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;
    private String trainNumber;
    private int trainSeats;
    private String trainDepartureTime;

    /**
     * Edit train data in database.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Train train = new Train();
        if (user != null && "admin".equals(user.getRole()) && checkParametersForCorrectness(request)) {
            try(Connection connection = DBManager.getInstance().getConnection()) {
                train.setId(trainId);
                train.setNumber(trainNumber);
                train.setSeats(trainSeats);
                train.setDepartureTime(LocalTime.parse(trainDepartureTime));
                trainDAO.editTrain(connection, train);
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("successMessage", "Train data has been edited");
                } else {
                    session.setAttribute("successMessage", "Дані поїзда змінено");
                }
            } catch (SQLException e) {
                logger.info("Failed to connect to database for edit train data  in database");
                if ("en".equals(session.getAttribute("locale"))) {
                    throw new DBException("Failed to connect to database for edit train data in database");
                } else {
                    throw new DBException("Не вийшло зв'язатися з базою даних, щоб відредагувати дані поїзда");
                }
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String stringTrainSeats = request.getParameter("trainSeats");
        trainDepartureTime = request.getParameter("trainDepartureTime");
        if (trainDepartureTime == null || trainDepartureTime.equals("")) {
            logger.info("Train departure time is not specified");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Train departure time is not specified");
            } else {
                session.setAttribute("errorMessage", "Час відправлення поїзда не задано");
            }
            return false;
        }
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
            trainSeats = Integer.parseInt(stringTrainSeats);
            for (String time:
                 trainDepartureTime.split(":")) {
                Integer.parseInt(time);
            }
        } catch (NumberFormatException e) {
            logger.info("Link data is incorrect", e);
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Request error, try again");
            } else {
                session.setAttribute("errorMessage", "Помилка при запиті, спробуйте ще раз");
            }
        }
        trainNumber = request.getParameter("trainNumber");
        if (trainNumber == null || trainNumber.equals("")) {
            logger.info("Train number is not specified");
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Train number is not specified");
            } else {
                session.setAttribute("errorMessage", "Номер поїзда не задано");
            }
            return false;
        }
        return true;
    }
}
