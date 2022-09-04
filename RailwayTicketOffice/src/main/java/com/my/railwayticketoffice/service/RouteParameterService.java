package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that verify station parameters
 *
 * @author Yevhen Pashchenko
 */
public class RouteParameterService implements ParameterService<String> {

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
        String timeSinceStart = parameters.get("timeSinceStart");
        String stopTime = parameters.get("stopTime");
        String distanceFromStart = parameters.get("distanceFromStart");
        if (timeSinceStart == null || stopTime == null || distanceFromStart == null) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("routeErrorMessage", "Train route parameters are not specified");
            } else {
                session.setAttribute("routeErrorMessage", "Параметри маршруту поїзда не задано");
            }
            return false;
        }
        if (timeSinceStart.split(":").length != 2) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("routeErrorMessage", "Time from departure of the train from the first station is set incorrectly");
            } else {
                session.setAttribute("routeErrorMessage", "Час від відправлення поїзда з першої станції задано неправильно");
            }
            return false;
        }
        try {
            for (String time:
                    timeSinceStart.split(":")) {
                Integer.parseInt(time);
            }
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("routeErrorMessage", "Time from departure of the train from the first station is set incorrectly");
            } else {
                session.setAttribute("routeErrorMessage", "Час від відправлення поїзда з першої станції задано неправильно");
            }
            return false;
        }
        if (stopTime.split(":").length != 2) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("routeErrorMessage", "Time stop of the train on the station is set incorrectly");
            } else {
                session.setAttribute("routeErrorMessage", "Час зупинки поїзда на станції задано неправильно");
            }
            return false;
        }
        try {
            for (String time:
                    stopTime.split(":")) {
                Integer.parseInt(time);
            }
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("routeErrorMessage", "Time stop of the train on the station is set incorrectly");
            } else {
                session.setAttribute("routeErrorMessage", "Час зупинки поїзда на станції задано неправильно");
            }
            return false;
        }
        try {
            Integer.parseInt(parameters.get("distanceFromStart"));
        } catch (NumberFormatException e) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("routeErrorMessage", "Distance from the first station is set incorrectly");
            } else {
                session.setAttribute("routeErrorMessage", "Відстань до першої станції задано неправильно");
            }
            return false;
        }
        return true;
    }
}
