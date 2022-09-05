package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that verify train parameters
 *
 * @author Yevhen Pashchenko
 */
public class TrainParameterService implements ParameterService<String> {

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
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
        if (parameters.containsKey("trainNumber")) {
            String trainNumber = parameters.get("trainNumber");
            if (trainNumber == null || "".equals(trainNumber)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Train number is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Номер поїзда не задано");
                }
                return false;
            }
            Pattern pattern = Pattern.compile("[\u0400-\u04FF\\p{Punct}\\p{Space}\\p{Digit}]*");
            Matcher matcher = pattern.matcher(trainNumber);
            if (!matcher.matches()) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "The train number should not contain letters other than Ukrainian");
                } else {
                    session.setAttribute("trainErrorMessage", "В номері поїзда не має бути інших букв, крім українських");
                }
                return false;
            }
        }
        if (parameters.containsKey("trainDepartureTime")) {
            String trainDepartureTime = parameters.get("trainDepartureTime");
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
        if (parameters.containsKey("carriageId")) {
            try {
                Integer.parseInt(parameters.get("carriageId"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Carriage number is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Номер вагона не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("carriageNumber")) {
            try {
                Integer.parseInt(parameters.get("carriageNumber"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Carriage number is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Номер вагона не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("newCarriageNumber")) {
            try {
                Integer.parseInt(parameters.get("newCarriageNumber"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "New carriage number is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Новий номер вагона не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("typeId")) {
            try {
                Integer.parseInt(parameters.get("typeId"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Carriage type is not specified or such type is not exists");
                } else {
                    session.setAttribute("trainErrorMessage", "Тип вагона не задано або такого типу не існує");
                }
                return false;
            }
        }
        if (parameters.containsKey("carriageType")) {
            String carriageType = parameters.get("carriageType");
            if (carriageType == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Carriage type is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Тип вагона не задано");
                }
                return false;
            }
            Pattern pattern = Pattern.compile("[\u0400-\u04FF\\p{Punct}\\p{Space}\\p{Digit}]*");
            Matcher matcher = pattern.matcher(carriageType);
            if (!matcher.matches()) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "The name of the carriage type should not contain letters other than Ukrainian");
                } else {
                    session.setAttribute("trainErrorMessage", "В назві типу вагона не має бути інших букв, крім українських");
                }
                return false;
            }
        }
        if (parameters.containsKey("newCarriageType")) {
            String carriageType = parameters.get("carriageType");
            String newCarriageType = parameters.get("newCarriageType");
            if (newCarriageType == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "New carriage type is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Нову назву типу вагона не задано");
                }
                return false;
            }
            Pattern pattern = Pattern.compile("[\u0400-\u04FF\\p{Punct}\\p{Space}\\p{Digit}]*");
            Matcher matcher = pattern.matcher(newCarriageType);
            if (!matcher.matches()) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "The new name of the carriage type should not contain letters other than Ukrainian");
                } else {
                    session.setAttribute("trainErrorMessage", "В новій назві типу вагона не має бути інших букв, крім українських");
                }
                return false;
            }
            if (newCarriageType.equals(carriageType)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Old carriage type and new carriage type are match");
                } else {
                    session.setAttribute("trainErrorMessage", "Нова та стара назви вагонів цього типу однакові");
                }
                return false;
            }
        }
        if (parameters.containsKey("maxSeats")) {
            try {
                Integer.parseInt(parameters.get("maxSeats"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("trainErrorMessage", "Maximum number seats this carriage type is not specified");
                } else {
                    session.setAttribute("trainErrorMessage", "Максимальну кількість місць вагона цього типу не задано");
                }
                return false;
            }
        }
        return true;
    }
}
