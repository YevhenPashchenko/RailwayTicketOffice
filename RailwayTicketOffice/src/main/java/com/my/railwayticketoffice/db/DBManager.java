package com.my.railwayticketoffice.db;

import com.my.railwayticketoffice.db.dao.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class represents a manager for a MySQL database. You can use {@link #getInstance()}
 * to obtain an instance. You can obtain DAO's using the DAO getters.
 * This class requires a properties file named 'db.properties' in the classpath with the
 * following property: dbName.
 *
 * @author Yevhen Pashchenko
 */

public class DBManager {

    private static final Logger logger = LogManager.getLogger(DBManager.class);

    private static final String DB_NAME = "dbName";
    private static DBManager instance;
    private final DataSource dataSource;

    private DBManager() {
        dataSource = getDataSource();
    }

    /**
     * Returns a DataSource implementation.
     * @return a DataSource implementation.
     * @throws IllegalStateException If the database name is incorrect or if the properties file
     * is missing in classpath or cannot be loaded.
     */
    private DataSource getDataSource() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            String dbName = properties.getProperty(DB_NAME);
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            return (DataSource) envContext.lookup("jdbc/" + dbName);
        } catch (NamingException e) {
            logger.error("Failed to find database with name which wrote in file db.properties", e);
            throw new IllegalStateException();
        } catch (IOException e) {
            logger.error("Failed to read db.properties file", e);
            throw new IllegalStateException();
        }
    }

    /**
     * Returns a DBManager instance (new if not exist yet).
     * @return a DBManager instance (new if not exist yet).
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public ScheduleDAO getScheduleDAO() {
        return new MySQLScheduleDAO();
    }

    public TrainDAO getTrainDAO() {
        return new MySQLTrainDAO();
    }

    public StationDAO getStationDAO() {
        return new MySQLStationDAO();
    }

    public UserDAO getUserDAO() {
        return new MySQLUserDAO();
    }

    /**
     * Wrapper for standard method Connection object rollback() for avoid try-catch blocks in other classes.
     * Undoes all changes made in the current transaction and releases any database locks currently held
     * by this Connection object. This method should be used only when auto-commit mode has been disabled.
     * @param connection - Connection object.
     * @param e - exception that is cause of call this method.
     * @throws DBException if method executed successfully or new {@link SQLException} was catch in process or
     * Connection object is null.
     */
    public void rollback(Connection connection, SQLException e) throws DBException {
        if (connection != null) {
            try {
                connection.rollback();
                logger.info("Transaction was rollback", e);
                throw new DBException("Failed to change data, please try again");
            } catch (SQLException ex) {
                logger.warn("Failed to rollback transaction", ex);
                throw new DBException("Failed to change data, please try again");
            }
        }
        logger.warn("Failed to get connection when call rollback method", e);
        throw new DBException("Failed to change data, please try again");
    }

    /**
     * Wrapper for standard method Connection object close() for avoid try-catch blocks in other classes.
     * Releases this Connection object's database and JDBC resources immediately. This method should be used only
     * when auto-commit mode has been disabled.
     * @param connection - Connection object.
     */
    public void close(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                logger.warn("Failed to close connection to database", e);
            }
        } else {
            logger.warn("Failed to get connection when call close method");
        }
    }
}
