package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.TrainParameterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Delete carriage from train.
 */
public class DeleteCarriageFromTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteCarriageFromTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Delete carriage from train.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     * @throws DBException if {@link SQLException} occurs.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws DBException {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null && "admin".equals(user.getRole())) {
            parameters.put("trainId", request.getParameter("trainId"));
            parameters.put("carriageNumber", request.getParameter("carriageNumber"));
            parameters.put("typeId", request.getParameter("typeId"));
            if (trainService.check(parameters, session)) {
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    int isTrainHasCarriageWithThisNumber = trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageNumber")));
                    if (isTrainHasCarriageWithThisNumber > 0) {
                        connection.setAutoCommit(false);
                        trainDAO.deleteCarriageFromTrain(connection, Integer.parseInt(parameters.get("trainId")), isTrainHasCarriageWithThisNumber);
                        scheduleDAO.deleteCarriageFromSchedule(connection, Integer.parseInt(parameters.get("trainId")), isTrainHasCarriageWithThisNumber);
                        connection.setAutoCommit(true);
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Carriage has been deleted from train");
                        } else {
                            session.setAttribute("successMessage", "Вагон видалено з поїзда");
                        }
                    } else {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "There is no carriage with this number in the train");
                        } else {
                            session.setAttribute("errorMessage", "В поїзді немає вагона з таким номером");
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database to delete carriage from train");
                    DBManager.getInstance().rollback(session, connection, e);
                } finally {
                    DBManager.getInstance().close(connection);
                }
            }
        }
        return "controller?command=mainPage";
    }
}
