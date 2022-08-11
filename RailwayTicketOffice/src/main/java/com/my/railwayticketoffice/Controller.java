package com.my.railwayticketoffice;

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
        req.setCharacterEncoding("UTF-8");
        String address = "error.jsp";
        Command command;
        String commandName = req.getParameter("command");
        try {
            command = CommandContainer.getCommand(commandName);
            address = command.execute(req, resp);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            req.setAttribute("errorMessage", e.getMessage());
        }
        req.getRequestDispatcher(address).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}