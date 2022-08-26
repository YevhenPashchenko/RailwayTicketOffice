package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that built at command pattern. Delete train from schedule.
 */
public class DeleteTrainFromSchedule implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteTrainFromSchedule.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private int trainId;

    /**
     * Delete train from schedule.
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
                    scheduleDAO.deleteTrainFromSchedule(connection, trainId);
                    session.setAttribute("successMessage", "Поїзд видалено з розкладу");
                } else {
                    session.setAttribute("errorMessage", "Поїзда немає в розкладі");
                }
            } catch (SQLException e) {
                logger.warn("Failed to delete train from schedule", e);
                throw new DBException("Failed to delete train from schedule");
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
