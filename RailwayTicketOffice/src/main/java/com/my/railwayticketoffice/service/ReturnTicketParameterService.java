package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class ReturnTicketParameterService implements ParameterService<String>{

    @Override
    public boolean check(Map<String, String> parameters, HttpSession session) {
        String ticketParameters = parameters.get("ticketNumber");
        if (ticketParameters != null && ticketParameters.split("-").length == 5 && ticketParameters.split("-")[3].length() == 12) {
            try {
                Integer.parseInt(ticketParameters.split("-")[0]);
                Integer.parseInt(ticketParameters.split("-")[1]);
                Integer.parseInt(ticketParameters.split("-")[2]);
                Long.parseLong(ticketParameters.split("-")[3]);
            } catch (NumberFormatException e) {
                if ("en".equals(session.getAttribute("locale"))) {
                    session.setAttribute("returnTicketErrorMessage", "Ticket number incorrect");
                } else {
                    session.setAttribute("returnTicketErrorMessage", "Номер квитка не коректний");
                }
                return false;
            }
            return true;
        }
        if ("en".equals(session.getAttribute("locale"))) {
            session.setAttribute("returnTicketErrorMessage", "Ticket number incorrect");
        } else {
            session.setAttribute("returnTicketErrorMessage", "Номер квитка не коректний");
        }
        return false;
    }
}
