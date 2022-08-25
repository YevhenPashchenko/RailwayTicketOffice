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
                session.setAttribute("successMessage", "Дані поїзда змінено успішно");
            } catch (SQLException e) {
                logger.info("Failed to edit train data in database");
                throw new DBException("Failed to edit train data in database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String stringTrainSeats = request.getParameter("trainSeats");
        trainDepartureTime = request.getParameter("trainDepartureTime");
        if (trainDepartureTime == null || trainDepartureTime.equals("")) {
            session.setAttribute("errorMessage", "Час відправлення поїзда має існувати та не має бути пустим");
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
            session.setAttribute("errorMessage", "Помилковий id поїзда або помилкове число місць поїзда або помилковий час відправлення поїзда");
        }
        trainNumber = request.getParameter("trainNumber");
        if (trainNumber == null || trainNumber.equals("")) {
            session.setAttribute("errorMessage", "Номер поїзда має існувати та не може бути пустим");
            return false;
        }
        return true;
    }
}
