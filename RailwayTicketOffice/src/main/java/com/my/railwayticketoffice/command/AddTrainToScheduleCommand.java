package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import com.my.railwayticketoffice.entity.User;
import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.ScheduleService;
import com.my.railwayticketoffice.service.TrainParameterService;
import com.my.railwayticketoffice.service.TrainScheduleService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Class that built at command pattern. Add train to the schedule.
 */
public class AddTrainToScheduleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddTrainToScheduleCommand.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ScheduleService service = new TrainScheduleService();

    /**
     * Add train to the schedule.
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
                    if (isExists) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "Train has already been added to the schedule");
                        } else {
                            session.setAttribute("errorMessage", "Поїзд вже додано до розкладу");
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
                    Train train = trainDAO.getTrain(connection, Integer.parseInt(parameters.get("trainId")));
                    trainDAO.getCarriagesForTrains(connection, Collections.singletonList(train));
                    if (train.getCarriages().size() == 0) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "The train has no carriages");
                        } else {
                            session.setAttribute("errorMessage", "Поїзд не має вагонів");
                        }
                        return "controller?command=mainPage";
                    }
                    List<String> scheduleDates = service.create();
                    scheduleDAO.addData(connection, scheduleDates, Collections.singletonList(train), null);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "Train has been added to schedule");
                    } else {
                        session.setAttribute("successMessage", "Поїзд додано до розкладу");
                    }
                } catch (SQLException e) {
                    logger.warn("Failed to connect to database for add train to schedule", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for add train to schedule");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб додати поїзд до розкладу");
                    }
                    throw new DBException("Failed to connect to database for add train to schedule");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
