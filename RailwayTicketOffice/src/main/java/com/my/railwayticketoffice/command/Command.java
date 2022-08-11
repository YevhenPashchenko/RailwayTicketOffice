package com.my.railwayticketoffice.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface that provides a method for classes that built at command pattern.
 *
 * @author Yevhen Pashchenko
 */
public interface Command {

    /**
     * Method implemented by classes that built at command pattern.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return string that must be a JSP file name or new request.
     */
    String execute(HttpServletRequest request, HttpServletResponse response);
}
