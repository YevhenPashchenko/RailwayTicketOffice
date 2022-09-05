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
 * Class that built at command pattern. Add carriage to train.
 *
 * @author Yevhen Pashchenko
 */
public class AddCarriageToTrainCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddCarriageToTrainCommand.class);
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final ParameterService<String> trainService = new TrainParameterService();
    private final ScheduleService scheduleService = new TrainScheduleService();

    /**
     * Add carriage to train.
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
            parameters.put("trainNumber", request.getParameter("trainNumber"));
            parameters.put("carriageNumber", request.getParameter("carriageNumber"));
            parameters.put("typeId", request.getParameter("typeId"));
            parameters.put("carriageType", request.getParameter("carriageType"));
            if (trainService.check(parameters, session)) {
                int carriageId = 0;
                try(Connection connection = DBManager.getInstance().getConnection()) {
                    int isTrainExist = trainDAO.checkIfTrainExists(connection, parameters.get("trainNumber"));
                    if (isTrainExist == 0 || isTrainExist != Integer.parseInt(parameters.get("trainId"))) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "Train with this number is not specified");
                        } else {
                            session.setAttribute("errorMessage", "Поїзда з таким номером не існує");
                        }
                        return "controller?command=mainPage";
                    }
                    int isTrainHasCarriageWithThisNumber = trainDAO.checkIfTrainHasCarriageWithThisNumber(connection, Integer.parseInt(parameters.get("trainId")), Integer.parseInt(parameters.get("carriageNumber")));
                    if (isTrainHasCarriageWithThisNumber > 0) {
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("errorMessage", "Train already has carriage with this number");
                        } else {
                            session.setAttribute("errorMessage", "Поїзд вже має вагон з таким номером");
                        }
                    } else {
                        Map<Integer, String> carriagesTypes = trainDAO.getCarriagesTypes(connection);
                        int typeId = Integer.parseInt(parameters.get("typeId"));
                        if (!carriagesTypes.containsKey(typeId) &&
                                !parameters.get("carriageType").equals(carriagesTypes.get(typeId))) {
                            if ("en".equals(session.getAttribute("locale"))) {
                                session.setAttribute("errorMessage", "Carriage with this type is not specified");
                            } else {
                                session.setAttribute("errorMessage", "Вагона такого типу не існує");
                            }
                            return "controller?command=mainPage";
                        }
                        int isCarriageExists = trainDAO.checkIfCarriageExists(connection, Integer.parseInt(parameters.get("carriageNumber")), typeId);
                        if (isCarriageExists == 0) {
                            carriageId = trainDAO.createCarriage(connection, Integer.parseInt(parameters.get("carriageNumber")), typeId);
                        } else {
                            carriageId = isCarriageExists;
                        }
                    }
                    boolean isTrainInSchedule = scheduleDAO.checkIfRecordExists(connection, Integer.parseInt(parameters.get("trainId")));
                    if (!isTrainInSchedule) {
                        trainDAO.addCarriageToTrain(connection, Integer.parseInt(parameters.get("trainId")), carriageId);
                        if ("en".equals(session.getAttribute("locale"))) {
                            session.setAttribute("successMessage", "Carriage added to train");
                        } else {
                            session.setAttribute("successMessage", "Вагон додано до поїзда");
                        }
                        return "controller?command=mainPage";
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for add carriage to train");
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("errorMessage", "Failed to connect to database for add carriage to train");
                    } else {
                        session.setAttribute("errorMessage", "Не вийшло зв'язатися з базою даних, щоб додати вагон до поїзда");
                    }
                    throw new DBException("Failed to connect to database for add carriage to train");
                }
                Connection connection = null;
                try {
                    connection = DBManager.getInstance().getConnection();
                    connection.setAutoCommit(false);
                    trainDAO.addCarriageToTrain(connection, Integer.parseInt(parameters.get("trainId")), carriageId);
                    List<String> scheduleDates = scheduleService.create();
                    int maxSeats = trainDAO.getCarriageMaxSeats(connection, carriageId);
                    Train train = new Train();
                    train.setId(Integer.parseInt(parameters.get("trainId")));
                    Train.Carriage carriage = train.new Carriage();
                    carriage.setId(carriageId);
                    carriage.setMaxSeats(maxSeats);
                    train.addCarriage(carriageId, carriage);
                    scheduleDAO.addData(connection, scheduleDates, Collections.singletonList(train));
                    connection.setAutoCommit(true);
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("successMessage", "Carriages added to train");
                    } else {
                        session.setAttribute("successMessage", "Вагон додано до поїзда");
                    }
                } catch (SQLException e) {
                    logger.info("Failed to connect to database for add carriage to train");
                    DBManager.getInstance().rollback(session, connection, e);
                } finally {
                    DBManager.getInstance().close(connection);
                }
            }
        }
        return "controller?command=mainPage";
    }
}
