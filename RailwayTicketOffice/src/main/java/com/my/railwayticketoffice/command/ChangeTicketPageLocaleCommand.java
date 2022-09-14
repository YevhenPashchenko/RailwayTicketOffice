package com.my.railwayticketoffice.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that built at command pattern. Change locale on ticket.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class ChangeTicketPageLocaleCommand implements Command {

    /**
     * Change locale on ticket.jsp.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link TicketPageCommand}.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = request.getParameter("locale");
        if (locale != null && !"".equals(locale)) {
            request.getSession().setAttribute("locale", locale);
        }
        request.setAttribute("trainId", request.getParameter("trainId"));
        request.setAttribute("from", request.getParameter("from"));
        request.setAttribute("to", request.getParameter("to"));
        request.setAttribute("departureDate", request.getParameter("departureDate"));
        request.setAttribute("carriageType", request.getParameter("carriageType"));
        request.setAttribute("carriage", request.getParameterValues("carriage"));
        request.setAttribute("seat", request.getParameterValues("seat"));
        request.setAttribute("cost", request.getParameterValues("cost"));
        return "controller?command=ticketPage";
    }
}
