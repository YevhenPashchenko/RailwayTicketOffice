package com.my.railwayticketoffice.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class that built at command pattern. Change locale on main.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class ChangeMainPageLocaleCommand implements Command {

    /**
     * Change locale on main.jsp.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand} or link to train chooses command if additional parameters exists.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String locale = request.getParameter("locale");
        if (locale != null && !"".equals(locale)) {
            request.getSession().setAttribute("locale", locale);
        }
        return chooseLink(request);
    }

    private String chooseLink(HttpServletRequest request) {
        boolean isAdditionalParameters = true;
        String link;
        String trainsSortedCommand = request.getParameter("trainsSortedCommand");
        int page = 0;
        int from = 0;
        int to = 0;
        String datePicker = request.getParameter("datePicker");
        if (trainsSortedCommand == null || "".equals(trainsSortedCommand)) {
            isAdditionalParameters = false;
        }
        try {
            page = Integer.parseInt(request.getParameter("page"));
            from = Integer.parseInt(request.getParameter("from"));
            to = Integer.parseInt(request.getParameter("to"));
            if (datePicker != null && datePicker.split("\\.").length == 3) {
                for (String date:
                     datePicker.split("\\.")) {
                    Integer.parseInt(date);
                }
            } else {
                isAdditionalParameters = false;
            }
        } catch (NumberFormatException e) {
            isAdditionalParameters = false;
        }
        if (isAdditionalParameters) {
            link = "controller?command=" + trainsSortedCommand + "&page=" + page + "&from=" + from + "&to=" + to + "&datePicker=" + datePicker;
        } else {
            link = "controller?command=mainPage";
        }
        return link;
    }
}
