package com.my.railwayticketoffice.command;

import com.my.railwayticketoffice.service.ParameterService;
import com.my.railwayticketoffice.service.SearchTrainParameterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that built at command pattern. Change locale on main.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class ChangeMainPageLocaleCommand implements Command {

    private final ParameterService<String> searchTrainService = new SearchTrainParameterService();

    /**
     * Change locale on main.jsp.
     * @param request - HttpServletRequest object.
     * @param response - HttpServletResponse object.
     * @return link to {@link MainPageCommand} or link to train chooses command if additional parameters exists.
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> parameters = new HashMap<>();
        HttpSession session = request.getSession();
        String locale = request.getParameter("locale");
        if (locale != null && !"".equals(locale)) {
            request.getSession().setAttribute("locale", locale);
        }
        parameters.put("page", request.getParameter("page"));
        parameters.put("from", request.getParameter("from"));
        parameters.put("to", request.getParameter("to"));
        parameters.put("date", request.getParameter("departureDate"));
        if (searchTrainService.check(parameters, session)) {
            return "controller?command=getTrains&page=" + Integer.parseInt(parameters.get("page")) + "&from=" + Integer.parseInt(parameters.get("from")) + "&to=" + Integer.parseInt(parameters.get("to")) + "&departureDate=" + parameters.get("date");
        } else {
            session.removeAttribute("searchTrainErrorMessage");
            return "controller?command=mainPage";
        }
    }
}
