package com.my.railwayticketoffice.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that built at command pattern. Change locale on route.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class ChangeRoutePageLocaleCommand implements Command {

    /**
     * Change locale on route.jsp.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link ShowRouteCommand}.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = request.getParameter("locale");
        if (locale != null && !"".equals(locale)) {
            request.getSession().setAttribute("locale", locale);
        }
        return "controller?command=showRoute";
    }
}
