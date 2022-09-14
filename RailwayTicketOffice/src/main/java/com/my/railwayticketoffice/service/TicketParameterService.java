package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Class that verify passenger parameters
 *
 * @author Yevhen Pashchenko
 */
public class TicketParameterService implements ParameterService<String[]>{

    @Override
    public boolean check(Map<String, String[]> parameters, HttpSession session) {
        if (parameters.containsKey("carriage") || parameters.containsKey("seat") || parameters.containsKey("cost")) {
            if (parameters.get("carriage") == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("passengerErrorMessage", "Carriage number is incorrect");
                } else {
                    session.setAttribute("passengerErrorMessage", "Номер вагона не коректний");
                }
                return false;
            }
            for (String carriage:
                    parameters.get("carriage")) {
                try {
                    Integer.parseInt(carriage);
                } catch (NumberFormatException e) {
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("passengerErrorMessage", "Carriage number is incorrect");
                    } else {
                        session.setAttribute("passengerErrorMessage", "Номер вагона не коректний");
                    }
                    return false;
                }
            }
            if (parameters.get("seat") == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("passengerErrorMessage", "Seat number is incorrect");
                } else {
                    session.setAttribute("passengerErrorMessage", "Номер місця не коректний");
                }
                return false;
            }
            for (String seat:
                    parameters.get("seat")) {
                try {
                    Integer.parseInt(seat);
                } catch (NumberFormatException e) {
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("passengerErrorMessage", "Seat number is incorrect");
                    } else {
                        session.setAttribute("passengerErrorMessage", "Номер місця не коректний");
                    }
                    return false;
                }
            }
            if (parameters.get("cost") == null) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("passengerErrorMessage", "Ticket cost is incorrect");
                } else {
                    session.setAttribute("passengerErrorMessage", "Вартість квитка не коректна");
                }
                return false;
            }
            for (String cost:
                    parameters.get("cost")) {
                if (cost == null) {
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("passengerErrorMessage", "Ticket cost is incorrect");
                    } else {
                        session.setAttribute("passengerErrorMessage", "Вартість квитка не коректна");
                    }
                    return false;
                }
                try {
                    Double.parseDouble(cost);
                } catch (NumberFormatException e) {
                    if ("en".equals(session.getAttribute("locale"))) {
                        session.setAttribute("passengerErrorMessage", "Ticket cost is incorrect");
                    } else {
                        session.setAttribute("passengerErrorMessage", "Вартість квитка не коректна");
                    }
                    return false;
                }
            }
        }
        if (parameters.containsKey("surname") || parameters.containsKey("name")) {
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
        }
        return true;
    }
}
