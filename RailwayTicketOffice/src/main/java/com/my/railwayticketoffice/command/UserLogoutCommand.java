package com.my.railwayticketoffice.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that built at command pattern. Invalidate session.
 *
 * @author Yevhen Pashchenko
 */
public class UserLogoutCommand implements Command {

    /**
     * Invalidate session.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand}.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return "controller?command=mainPage";
    }
}
