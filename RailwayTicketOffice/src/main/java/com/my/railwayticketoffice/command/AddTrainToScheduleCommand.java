package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class that built at command pattern. Add train to the schedule.
 */
public class AddTrainToScheduleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddTrainToScheduleCommand.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;

    /**
     * Add train to the schedule.
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
                boolean isExists = scheduleDAO.checkIfRecordExists(connection, trainId);
                if (isExists) {
                    session.setAttribute("errorMessage", "Поїзд вже додано до розкладу");
                } else {
                    Train train = trainDAO.getTrain(connection, trainId);
                    if (train.getDepartureTime() != null) {
                        int scheduleDurationForTrain;
                        LocalTime localTimeNow = LocalTime.now();
                        LocalDate date = LocalDate.now();
                        if (localTimeNow.isBefore(train.getDepartureTime())) {
                            scheduleDurationForTrain = Util.getScheduleDuration();
                        } else {
                            scheduleDurationForTrain = Util.getScheduleDuration() - 1;
                            date = date.plusDays(1);
                        }
                        List<String> scheduleDates = new ArrayList<>();
                        for (int i = 0; i < scheduleDurationForTrain; i++) {
                            String currentDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                            scheduleDates.add(currentDate);
                            date = date.plusDays(1);
                        }
                        scheduleDAO.addData(connection, scheduleDates, Collections.singletonList(train));
                        session.setAttribute("successMessage", "Поїзд додано до розкладу");
                    } else {
                        session.setAttribute("errorMessage", "Виберіть існуючий поїзд");
                    }
                }
            } catch (SQLException e) {
                logger.warn("Failed to add train to schedule", e);
                throw new DBException("Failed to add train to schedule");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
        } catch (NumberFormatException e) {
            logger.info("Train id is incorrect");
            session.setAttribute("errorMessage", "Виберіть існуючий поїзд");
            return false;
        }
        return true;
    }
}
