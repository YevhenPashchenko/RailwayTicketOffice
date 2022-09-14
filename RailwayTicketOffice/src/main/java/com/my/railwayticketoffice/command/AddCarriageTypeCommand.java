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
 * Class that built at command pattern. Add new carriage type to database.
 *
 * @author Yevhen Pashchenko
 */
public class AddCarriageTypeCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddCarriageTypeCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Add new carriage type to database.
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
            parameters.put("carriageType", request.getParameter("carriageType"));
            parameters.put("maxSeats", request.getParameter("maxSeats"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isCarriageTypeExists = trainDAO.checkIfCarriageTypeExists(connection, parameters.get("carriageType"));
                    if (isCarriageTypeExists > 0) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "This carriage type already exists");
                        } else {
                            session.setAttribute("errorMessage", "Такий тип вагона вже існує");
                        }
                    } else {
                        trainDAO.addCarriageType(connection, parameters.get("carriageType"), Integer.parseInt(parameters.get("maxSeats")));
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "New carriage type created");
                        } else {
                            session.setAttribute("successMessage", "Новий тип вагона створено");
                        }
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database to add new carriage type to database");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database to add new carriage type to database");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб додати новий тип вагона");
                    }
                    throw new DBException("Failed to connect to database to add new carriage type to database");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
