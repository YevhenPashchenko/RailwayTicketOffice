package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Pattern pattern = Pattern.compile("[\u0400-\u04FF\\p{Punct}\\p{Space}\\p{Digit}]*");
            Matcher matcher = pattern.matcher(stationNameUA);
            if (!matcher.matches()) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("stationErrorMessage", "Station name in Ukrainian should not contain letters other than Ukrainian");
                } else {
                    session.setAttribute("stationErrorMessage", "В назві станції українською не має бути інших букв, крім українських");
                }
                return false;
            }
            pattern = Pattern.compile("[A-Za-z\\p{Punct}\\p{Space}\\p{Digit}]*");
            matcher = pattern.matcher(stationNameEN);
            if (!matcher.matches()) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("stationErrorMessage", "Station name in English should not contain letters other than English");
                } else {
                    session.setAttribute("stationErrorMessage", "В назві станції англійською не має бути інших букв, крім англійських");
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
            if ("en".equals(session.getAttribute("locale"))) {
                Pattern pattern = Pattern.compile("[A-Za-z\\p{Punct}\\p{Space}\\p{Digit}]*");
                Matcher matcher = pattern.matcher(stationName);
                if (!matcher.matches()) {
                    session.setAttribute("stationErrorMessage", "Station name in English should not contain letters other than English");
                    return false;
                }
            } else {
                Pattern pattern = Pattern.compile("[\u0400-\u04FF\\p{Punct}\\p{Space}\\p{Digit}]*");
                Matcher matcher = pattern.matcher(stationName);
                if (!matcher.matches()) {
                    session.setAttribute("stationErrorMessage", "В назві станції українською не має бути інших букв, крім українських");
                    return false;
                }
            }
        }
        return true;
    }
}
