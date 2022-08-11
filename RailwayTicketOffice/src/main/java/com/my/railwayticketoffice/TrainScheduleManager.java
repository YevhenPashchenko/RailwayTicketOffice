package com.my.railwayticketoffice;

import com.my.railwayticketoffice.db.DBManager;
import com.my.railwayticketoffice.db.dao.ScheduleDAO;
import com.my.railwayticketoffice.db.dao.TrainDAO;
import com.my.railwayticketoffice.entity.Train;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class that managing train schedule.
 *
 * @author Yevhen Pashchenko
 */
public class TrainScheduleManager implements Runnable {

    private static final Logger logger = LogManager.getLogger(TrainScheduleManager.class);
    private final ScheduleDAO scheduleDAO = DBManager.getInstance().getScheduleDAO();
    private final TrainDAO trainDAO = DBManager.getInstance().getTrainDAO();

    /**
     * Prepares data for train schedule.
     */
    public void initialize() {
        try(Connection connection = DBManager.getInstance().getConnection()) {
            LocalDate date = LocalDate.now();
            List<String> scheduleDates = new ArrayList<>();
            for (int i = 0; i < Util.getScheduleDuration(); i++) {
                String currentDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                scheduleDates.add(currentDate);
                date = date.plusDays(1);
            }
            List<Train> trains = trainDAO.getAllTrainsDataForSchedule(connection);
            scheduleDAO.clearTable(connection);
            if (trains.size() > 0) {
                scheduleDAO.addData(connection, scheduleDates, trains);
            }
        } catch (SQLException e) {
            logger.warn("Failed to prepare train schedule", e);
        }
    }

    /**
     * Update the data for the train schedule at the beginning of a new day.
     */
    @Override
    public void run() {
        try(Connection connection = DBManager.getInstance().getConnection()) {
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
            String currentDate = date.format(formatter);
            String newLastScheduleDate = date.plusDays(Util.getScheduleDuration()).format(formatter);
            List<String> scheduleDates = new ArrayList<>();
            scheduleDAO.deleteData(connection, currentDate);
            scheduleDates.add(newLastScheduleDate);
            List<Train> trains = trainDAO.getAllTrainsDataForSchedule(connection);
            scheduleDAO.addData(connection, scheduleDates, trains);
        } catch (SQLException e) {
            logger.warn("Failed to update train schedule", e);
        }
    }
}
