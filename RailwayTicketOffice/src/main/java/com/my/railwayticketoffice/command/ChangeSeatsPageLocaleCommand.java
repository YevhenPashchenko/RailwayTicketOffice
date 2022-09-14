package com.my.railwayticketoffice.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class that built at command pattern. Change locale on seats.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class ChangeSeatsPageLocaleCommand implements Command {

    /**
     * Change locale on seats.jsp.
     * @param request HttpServletRequest object.
     * @param response HttpServletResponse object.
     * @return link to {@link ChooseSeatsPageCommand} if parameters are correct else link to {@link MainPageCommand}.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String locale = request.getParameter("locale");
        if (locale != null && !"".equals(locale)) {
            session.setAttribute("locale", locale);
        }
        request.setAttribute("trainId", request.getParameter("trainId"));
        request.setAttribute("from", request.getParameter("from"));
        request.setAttribute("to", request.getParameter("to"));
        request.setAttribute("departureDate", request.getParameter("departureDate"));
        request.setAttribute("carriageType", request.getParameter("carriageType"));
        request.setAttribute("carriageNumber", request.getParameter("carriageNumber"));
        if (request.getParameterMap().get("carriage") != null) {
            request.setAttribute("carriage", request.getParameterValues("carriage"));
            request.setAttribute("seat", request.getParameterValues("seat"));
            request.setAttribute("cost", request.getParameterValues("cost"));
        }
        return "controller?command=chooseSeatsPage";
    }
}
