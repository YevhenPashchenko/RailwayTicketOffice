package com.my.railwayticketoffice;

import com.my.railwayticketoffice.authentication.AuthenticationException;
import com.my.railwayticketoffice.command.Command;
import com.my.railwayticketoffice.command.CommandContainer;
import com.my.railwayticketoffice.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

/**
 * Class controller that provides communication between view layer and model layer.
 *
 * @author Yevhen Pashchenko
 */
@WebServlet("/controller")
public class Controller extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(Controller.class);

    /**
     * Handles GET request and passes it for processing to the class
     * object that is built at the command pattern.
     * @param req - HttpServletRequest object.
     * @param resp - HttpServletResponse object.
     * @throws ServletException – if a servlet error occurs.
     * @throws IOException – if a failed or interrupted I/O operations occurs.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String address = handleRequest(req, resp);
        req.getRequestDispatcher(address).forward(req, resp);
    }

    /**
     * Handles GET request and passes it for processing to the class
     * object that is built at the command pattern.
     * @param req - HttpServletRequest object.
     * @param resp - HttpServletResponse object.
     * @throws IOException – if a failed or interrupted I/O operations occurs.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String address = handleRequest(req, resp);
        resp.sendRedirect(address);
    }

    private String handleRequest(HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String address = "error.jsp";
        Command command;
        String commandName = req.getParameter("command");
        try {
            command = CommandContainer.getCommand(commandName);
            address = command.execute(req, resp);
        } catch (DBException | AuthenticationException e) {
            logger.warn(e.getMessage(), e);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            if ("en".equals(session.getAttribute("locale"))) {
                session.setAttribute("errorMessage", "Request data is incorrect");
            } else {
                session.setAttribute("errorMessage", "Некоректні дані в запиті");
            }
        }
        return address;
    }
}