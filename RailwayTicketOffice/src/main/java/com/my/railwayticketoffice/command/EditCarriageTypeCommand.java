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
 * Class that built at command pattern. Edit carriage type.
 *
 * @author Yevhen Pashchenko
 */
public class EditCarriageTypeCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddCarriageTypeCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ParameterService<String> trainService = new TrainParameterService();

    /**
     * Edit carriage type.
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
            parameters.put("newCarriageType", request.getParameter("newCarriageType"));
            if (trainService.check(parameters, session)) {
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    Map<Integer, String> types = trainDAO.getCarriagesTypes(connection);
                    if (!parameters.get("carriageType").equals(types.get(Integer.parseInt(request.getParameter("typeId"))))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "There are no carriages of this type in the database");
                        } else {
                            session.setAttribute("errorMessage", "В базі даних немає вагонів даного типу");
                        }
                        return "controller?command=mainPage";
                    }
                    for (String type:
                         types.values()) {
                        if (parameters.get("newCarriageType").equals(type)) {
                            if ("en".equals(session.getAttribute("locale"))) {
                                session.setAttribute("errorMessage", "Carriages of this type already exists");
                            } else {
                                session.setAttribute("errorMessage", "Вагони даного типу вже існують");
                            }
                            return "controller?command=mainPage";
                        }
                    }
                    trainDAO.editCarriageType(connection, parameters.get("newCarriageType"), Integer.parseInt(request.getParameter("typeId")));
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "Carriages type has been edited");
                    } else {
                        session.setAttribute("successMessage", "Назву типу вагонів змінено");
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database to edit carriage type");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database to edit carriage type");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб змінити назву типу вагона");
                    }
                    throw new DBException("Failed to connect to database to edit carriage type");
                }
            }
        }
        return "controller?command=mainPage";
    }
}
