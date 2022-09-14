package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.db.DBException;
import com.my.railwayticketoffice.db.DBManager;
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
 * Class that built at command pattern. Add train to database.
 *
 * @author Yevhen Pashchenko
 */
public class AddTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Add train to database.
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
            parameters.put("trainNumber", request.getParameter("trainNumber"));
            parameters.put("trainDepartureTime", request.getParameter("trainDepartureTime"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isTrainExists = trainDAO.checkIfTrainExists(connection, parameters.get("trainNumber"));
                    if (isTrainExists > 0) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "A train with this number already exists");
                        } else {
                            session.setAttribute("errorMessage", "Поїзд з таким номером вже існує");
                        }
                    } else {
                        trainDAO.addTrain(connection, parameters.get("trainNumber"), parameters.get("trainDepartureTime"));
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "New train has been created");
                        } else {
                            session.setAttribute("successMessage", "Новий поїзд створено");
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to add new train in database");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for create new train in database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб створити новий поїзд в базі даних");
                    }
                    throw new DBException("Failed to connect to database for create new train in database");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
