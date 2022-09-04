package com.my.railwayticketoffice.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class that implements {@link ServletContextListener} for receiving notification events about ServletContext lifecycle changes.
 * Method this class try to load locale properties for internationalization.
 *
 * @author Yevhen Pashchenko
 */
@WebListener
public class InternationalizationListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(InternationalizationListener.class);

    /**
     * Try to load locale properties for internationalization.
     * @param sce - ServletContextEvent object.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String localesFileName = context.getInitParameter("locales");

        String localesFileRealPath = context.getRealPath(localesFileName);

        Properties locales = new Properties();

        try(FileInputStream fis = new FileInputStream(localesFileRealPath)) {
            locales.load(fis);
        } catch (IOException e) {
            logger.error("Failed to load locale properties", e);
            throw new IllegalStateException("Failed to load locale properties", e);
        }

        context.setAttribute("locales", locales);
    }
}
