package com.my.railwayticketoffice.listener;

import com.my.railwayticketoffice.TrainScheduleManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that implements {@link ServletContextListener} for receiving notification events about ServletContext lifecycle changes.
 * Methods this class managing {@link TrainScheduleManager} object.
 *
 * @author Yevhen Pashchenko
 */
@WebListener
public class TrainScheduleListener implements ServletContextListener {

    private final TrainScheduleManager scheduleManager = new TrainScheduleManager();
    private ScheduledExecutorService scheduler;

    /**
     * Call a {@link TrainScheduleManager} method and create a scheduler which running once a day.
     * @param sce - ServletContextEvent object.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduleManager.initialize();
        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 10);
        calendar.set(Calendar.MILLISECOND, 0);
        long initialDelay = calendar.getTimeInMillis() - now;
        long periodInMillis = 24 * 60 * 60 * 1000;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(scheduleManager, initialDelay, periodInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * Shutdown scheduler that managed {@link TrainScheduleManager} object.
     * @param sce - ServletContextEvent object.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        scheduler.shutdownNow();
    }
}
