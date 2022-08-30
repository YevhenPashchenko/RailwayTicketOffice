package com.my.railwayticketoffice.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that built at command pattern. Change locale on success.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class ChangeSuccessPageLocaleCommand implements Command {

    /**
     * Change locale on success.jsp.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to success.jsp.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = request.getParameter("locale");
        if (locale != null && !"".equals(locale)) {
            request.getSession().setAttribute("locale", locale);
        }
        return "success.jsp";
    }
}
