package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that verify train parameters
 *
 * @author Yevhen Pashchenko
 */
public class TrainParameterService implements ParameterService<String> {

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
        if (parameters.containsKey("trainNumber") || parameters.containsKey("trainSeats") || parameters.containsKey("trainDepartureTime")) {
            String trainNumber = parameters.get("trainNumber");
            String trainDepartureTime = parameters.get("trainDepartureTime");
            if (trainNumber == null || "".equals(trainNumber)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train number is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Номер поїзда не задано");
                }
                return false;
            }
            try {
                Integer.parseInt(parameters.get("trainSeats"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train seats is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Кількість місць в поїзді не задано");
                }
                return false;
            }
            if (trainDepartureTime == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train departure time is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Час відправлення поїзда не задано");
                }
                return false;
            }
            if (trainDepartureTime.split(":").length != 2) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train departure time is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Час відправлення поїзда не задано");
                }
                return false;
            }
            try {
                for (String time:
                        trainDepartureTime.split(":")) {
                    Integer.parseInt(time);
                }
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train departure time is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Час відправлення поїзда не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("trainId")) {
            try {
                Integer.parseInt(parameters.get("trainId"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train name cannot be empty and train must exist");
                } else {
                    session.setAttribute("trainErrorMessage", "Ім'я поїзда не може бути пустим і поїзд має існувати");
                }
                return false;
            }
        }
        return true;
    }
}
