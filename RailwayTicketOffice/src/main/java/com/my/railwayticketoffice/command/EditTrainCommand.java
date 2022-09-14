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
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Edit train data in database.
 *
 * @author Yevhen Pashchenko
 */
public class EditTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(EditTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Edit train data in database.
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
            parameters.put("trainDepartureTime", request.getParameter("trainDepartureTime"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isTrainExists = trainDAO.checkIfTrainExists(connection, request.getParameter("oldTrainNumber"));
                    if (isTrainExists == 0 || isTrainExists != Integer.parseInt(parameters.get("trainId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "A train with this number no exists");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                    } else {
                        Train train = new Train();
                        train.setId(Integer.parseInt(parameters.get("trainId")));
                        train.setNumber(parameters.get("trainNumber"));
                        train.setDepartureTime(LocalTime.parse(parameters.get("trainDepartureTime")));
                        trainDAO.editTrain(connection, train);
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Train data has been edited");
                        } else {
                            session.setAttribute("successMessage", "Дані поїзда змінено");
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for edit train data  in database");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for edit train data in database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб відредагувати дані поїзда");
                    }
                    throw new DBException("Failed to connect to database for edit train data in database");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
