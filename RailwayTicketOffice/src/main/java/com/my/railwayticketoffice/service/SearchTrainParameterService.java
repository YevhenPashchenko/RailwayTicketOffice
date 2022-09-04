package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that verify search train parameters
 *
 * @author Yevhen Pashchenko
 */
public class SearchTrainParameterService implements ParameterService<String> {

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
        String from = parameters.get("from");
        String to = parameters.get("to");
        if (from == null || to == null) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("searchTrainErrorMessage", "Departure station or destination station is not specified");
            } else {
                session.setAttribute("searchTrainErrorMessage", "Станцію відправлення або призначення не задано");
            }
            return false;
        }
        try {
            Integer.parseInt(parameters.get("from"));
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("searchTrainErrorMessage", "Departure station is not specified");
            } else {
                session.setAttribute("searchTrainErrorMessage", "Станцію відправлення не задано");
            }
            return false;
        }
        try {
            Integer.parseInt(parameters.get("to"));
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("searchTrainErrorMessage", "Destination station is not specified");
            } else {
                session.setAttribute("searchTrainErrorMessage", "Станцію призначення не задано");
            }
            return false;
        }
        if (Integer.parseInt(parameters.get("from")) == Integer.parseInt(parameters.get("to"))) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("searchTrainErrorMessage", "Departure and destination stations match");
            } else {
                session.setAttribute("searchTrainErrorMessage", "Станції відправлення та призначення співпадають");
            }
            return false;
        }
        if (parameters.containsKey("date")) {
            String trainDepartureDate = parameters.get("date");
            if (trainDepartureDate == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("searchTrainErrorMessage", "Train departure date is not specified");
                } else {
                    session.setAttribute("searchTrainErrorMessage", "Дату відправлення поїзда не задано");
                }
                return false;
            }
            if (trainDepartureDate.split("\\.").length != 3) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("searchTrainErrorMessage", "Train departure date is not specified");
                } else {
                    session.setAttribute("searchTrainErrorMessage", "Дату відправлення поїзда не задано");
                }
                return false;
            }
            try {
                for (String date:
                        trainDepartureDate.split("\\.")) {
                    Integer.parseInt(date);
                }
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("searchTrainErrorMessage", "Train departure date is not specified");
                } else {
                    session.setAttribute("searchTrainErrorMessage", "Дату відправлення поїзда не задано");
                }
                return false;
            }
        }
        if (parameters.containsKey("trainsSortedCommand")) {
            String trainsSortedCommand = parameters.get("trainsSortedCommand");
            if (trainsSortedCommand == null || "".equals(trainsSortedCommand)) {
                return false;
            }
        }
        if (parameters.containsKey("page")) {
            try {
                Integer.parseInt(parameters.get("page"));
            } catch (NumberFormatException e) {
                parameters.put("page", "1");
            }
        }
        return true;
    }
}
