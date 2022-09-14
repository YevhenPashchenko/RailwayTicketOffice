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
 * Class that built at command pattern. Delete train from schedule.
 */
public class DeleteTrainFromScheduleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteTrainFromScheduleCommand.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Delete train from schedule.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
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
            parameters.put("trainNumber", request.getParameter("trainNumber"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    boolean isExists = scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(parameters.get("trainId")));
                    if (!isExists) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "There is no train on the schedule");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда немає в розкладі");
                        }
                        return "controller?command=mainPage";
                    }
                    int isTrainExists = trainDAO.checkIfTrainExists(connection, parameters.get("trainNumber"));
                    if (isTrainExists == 0 || isTrainExists != Integer.parseInt(parameters.get("trainId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "A train with this number no exists");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    scheduleDAO.deleteTrainFromSchedule(connection, Integer.parseInt(parameters.get("trainId")));
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "Train has been deleted from schedule");
                    } else {
                        session.setAttribute("successMessage", "Поїзд видалено з розкладу");
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for delete train from schedule", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for delete train from schedule");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити поїзд з розкладу");
                    }
                    throw new DBException("Failed to connect to database for delete train from schedule");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
