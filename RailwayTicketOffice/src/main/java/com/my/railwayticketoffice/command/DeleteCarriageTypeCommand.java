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
 * Class that built at command pattern. Delete carriage type from database.
 */
public class DeleteCarriageTypeCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteCarriageTypeCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     *
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
            parameters.put("typeId", request.getParameter("typeId"));
            parameters.put("carriageType", request.getParameter("carriageType"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isCarriageTypeExist = trainDAO.checkIfCarriageTypeExists(connection, parameters.get("carriageType"));
                    if (isCarriageTypeExist == 0 || isCarriageTypeExist != Integer.parseInt(parameters.get("typeId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "There are no carriage of this type in the database");
                        } else {
                            session.setAttribute("errorMessage", "В базі даних немає вагонів даного типу");
                        }
                    } else {
                        if (scheduleDAO.checkIfCarriagesIsInSchedule(connection, Integer.parseInt(parameters.get("typeId")))) {
                            if ("en".equals(session.getAttribute("locale"))) {
                                session.setAttribute("errorMessage", "Cannot be deleted, carriages this type is on the schedule");
                            } else {
                                session.setAttribute("errorMessage", "Неможливо видалити, вагони цього типу є в розкладі");
                            }
                        } else {
                            trainDAO.deleteCarriageType(connection, Integer.parseInt(parameters.get("typeId")));
                            if ("en".equals(session.getAttribute("locale"))) {
                                session.setAttribute("successMessage", "Carriages of this type have been deleted");
                            } else {
                                session.setAttribute("successMessage", "Вагони цього типу видалено");
                            }
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for delete carriage type from database", e);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for delete carriage type from database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб видалити вагони цього типу з бази даних");
                    }
                    throw new DBException("Failed to connect to database for delete carriage type from database");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
