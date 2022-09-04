package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that verify station parameters
 *
 * @author Yevhen Pashchenko
 */
public class StationParameterService implements ParameterService<String> {

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
        if (parameters.containsKey("stationNameUA") || parameters.containsKey("stationNameEN")) {
            String stationNameUA = parameters.get("stationNameUA");
            String stationNameEN = parameters.get("stationNameEN");
            if (stationNameUA == null || "".equals(stationNameUA) || stationNameEN == null || "".equals(stationNameEN)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("stationErrorMessage", "Station name cannot be empty");
                } else {
                    session.setAttribute("stationErrorMessage", "Ім'я станції не може бути пустим");
                }
                return false;
            }
        }
        if (parameters.containsKey("stationId")) {
            try {
                Integer.parseInt(parameters.get("stationId"));
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("stationErrorMessage", "Station name cannot be empty and station must exist");
                } else {
                    session.setAttribute("stationErrorMessage", "Ім'я станції не може бути пустим і станція має існувати");
                }
                return false;
            }
        }
        if (parameters.containsKey("stationName")) {
            String stationName = parameters.get("stationName");
            if (stationName == null || "".equals(stationName)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("stationErrorMessage", "Station name cannot be empty");
                } else {
                    session.setAttribute("stationErrorMessage", "Ім'я станції не може бути пустим");
                }
                return false;
            }
        }
        return true;
    }
}
