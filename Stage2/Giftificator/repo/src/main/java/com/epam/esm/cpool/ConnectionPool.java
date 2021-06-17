package com.epam.esm.cpool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class ConnectionPool {

//    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private final static String PROPERTIES_FILE_NAME = "/db.properties";

    private static final String URL = "url";
    private static final String DRIVER_CLASS_NAME = "driverClassName";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String TEST_ON_BORROW = "testOnBorrow";
    private static final String VALIDATION_QUERY = "validationQuery";
    private static final String VALIDATION_INTERVAL = "validationInterval";
    private static final String TIME_BETWEEN_EVICTIONRUNS_MILLIS = "timeBetweenEvictionRunsMillis";
    private static final String REMOVE_ABANDONED = "removeAbandoned";
    private static final String JDBC_INTERCEPTORS = "jdbcInterceptors";

    private final DataSource dataSource = new DataSource();

    private final Logger logger = LogManager.getLogger(ConnectionPool.class);

    private ConnectionPool() {
        try {
            InputStream is = this.getClass().getResourceAsStream(PROPERTIES_FILE_NAME);
            Properties properties = new Properties();
            properties.load(is);
            PoolProperties p = new PoolProperties();
            p.setUrl(properties.getProperty(URL));
            p.setDriverClassName(properties.getProperty(DRIVER_CLASS_NAME));
            p.setUsername(properties.getProperty(USERNAME));
            p.setPassword(properties.getProperty(PASSWORD));
            p.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty(TEST_ON_BORROW)));
            p.setValidationQuery(properties.getProperty(VALIDATION_QUERY));
            p.setValidationInterval(Long.parseLong(properties.getProperty(VALIDATION_INTERVAL)));
            p.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty(TIME_BETWEEN_EVICTIONRUNS_MILLIS)));
            p.setRemoveAbandoned(Boolean.parseBoolean(properties.getProperty(REMOVE_ABANDONED)));
            p.setJdbcInterceptors(properties.getProperty(JDBC_INTERCEPTORS));

            dataSource.setPoolProperties(p);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

//    public static ConnectionPool get() {
//        return INSTANCE;
//    }
    public static ConnectionPool get() {
        return new ConnectionPool();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}