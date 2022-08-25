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

/**
 * Class that built at command pattern. Delete train from database.
 *
 * @author Yevhen Pashchenko
 */
public class DeleteTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private int trainId;

    /**
     * Delete train from database.
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
                trainDAO.deleteTrain(connection, trainId);
                session.setAttribute("successMessage", "Поїзд видалено");
            } catch (SQLException e) {
                logger.info("Failed to delete train from database", e);
                throw new DBException("Failed to delete train from database");
            }
        }
        return "controller?command=mainPage";
    }

    private boolean checkParametersForCorrectness(HttpServletRequest request) {
        HttpSession session = request.getSession();
        try {
            trainId = Integer.parseInt(request.getParameter("trainId"));
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Номер поїзда не задано або поїзда з таким номером немає");
            return false;
        }
        return true;
    }
}
