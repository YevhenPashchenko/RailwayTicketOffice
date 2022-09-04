package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that verify passenger parameters
 *
 * @author Yevhen Pashchenko
 */
public class PassengerParameterService implements ParameterService<String[]>{

    @Override
    public boolean check(Map<String, String[]> parameters, HttpSession session) {
        if (parameters.get("surname") == null) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("passengerErrorMessage", "Passenger surname is not specified");
            } else {
                session.setAttribute("passengerErrorMessage", "Прізвище пасажира не задано");
            }
            return false;
        }
        for (String surname:
                parameters.get("surname")) {
            if (surname == null || "".equals(surname)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("passengerErrorMessage", "Passenger surname is not specified");
                } else {
                    session.setAttribute("passengerErrorMessage", "Прізвище пасажира не задано");
                }
                return false;
            }
        }
        if (parameters.get("name") == null) {
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("passengerErrorMessage", "Passenger name is not specified");
            } else {
                session.setAttribute("passengerErrorMessage", "Ім'я пасажира не задано");
            }
            return false;
        }
        for (String name:
                parameters.get("name")) {
            if (name == null || "".equals(name)) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("passengerErrorMessage", "Passenger name is not specified");
                } else {
                    session.setAttribute("passengerErrorMessage", "Ім'я пасажира не задано");
                }
                return false;
            }
        }
        return true;
    }
}
