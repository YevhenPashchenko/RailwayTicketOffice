package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
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
 * Class that built at command pattern. Switch auto addition train to the schedule.
 *
 * @author Yevhen Pashchenko
 */
public class SwitchAutoAdditionTrainToScheduleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SwitchAutoAdditionTrainToScheduleCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Switch auto addition train to the schedule.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @return link to {@link MainPageCommand}
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
                    int isTrainExists = trainDAO.checkIfTrainExists(connection, parameters.get("trainNumber"));
                    if (isTrainExists == 0 || isTrainExists != Integer.parseInt(parameters.get("trainId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "A train with this number no exists");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    Train train = trainDAO.getTrain(connection, Integer.parseInt(parameters.get("trainId")));
                    trainDAO.switchAutoAdditionTrainToSchedule(connection, train.getId(), !train.isInSchedule());
                    if (train.isInSchedule()) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Auto addition train to schedule disabled");
                        } else {
                            session.setAttribute("successMessage", "Автоматичне додавання поїзда до розкладу вимкнено");
                        }
                    } else {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Auto addition train to schedule enabled");
                        } else {
                            session.setAttribute("successMessage", "Автоматичне додавання поїзда до розкладу ввімкнено");
                        }
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for switch auto addition train to schedule", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for switch auto addition train to schedule");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб перемкнути автоматичне додавання поїзда до розкладу");
                    }
                    throw new DBException("Failed to connect to database for switch auto addition train to schedule");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
