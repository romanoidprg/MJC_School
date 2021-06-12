package com.epam.esm.cpool;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

@Component
public class ConnectionPool {

    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private final static String PROPERTIES_FILE_NAME = "db.properties";

    private static final String URL = "url";
    private static final String DRIVER_CLASS_NAME = "driverClassName";
    private static final String USERNAME = "username";
    private static final String JMX_ENABLED = "jmxEnabled";
    private static final String TEST_WHILE_IDLE = "testWhileIdle";
    private static final String PASSWORD = "password";
    private static final String TEST_ON_BORROW = "testOnBorrow";
    private static final String VALIDATION_QUERY = "validationQuery";
    private static final String TEST_ON_RETURN = "testOnReturn";
    private static final String VALIDATION_INTERVAL = "validationInterval";
    private static final String TIME_BETWEEN_EVICTIONRUNS_MILLIS = "timeBetweenEvictionRunsMillis";
    private static final String MAX_ACTIVE = "maxActive";
    private static final String INITIAL_SIZE = "initialSize";
    private static final String MAX_WAIT = "maxWait";
    private static final String REMOVE_ABANDONED_TIMEOUT = "removeAbandonedTimeout";
    private static final String MIN_EVICTABLE_IDLE_TIME_MILLIS = "minEvictableIdleTimeMillis";
    private static final String MIN_IDLE = "minIdle";
    private static final String LOG_ABANDONED = "logAbandoned";
    private static final String REMOVE_ABANDONED = "removeAbandoned";
    private static final String JDBC_INTERCEPTORS = "jdbcInterceptors";

    private final DataSource dataSource = new DataSource();

    Logger logger = LogManager.getLogger(ConnectionPool.class);

    private ConnectionPool() {
        try (FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource(PROPERTIES_FILE_NAME).getPath())) {
            Properties properties = new Properties();
            properties.load(fis);
            PoolProperties p = new PoolProperties();
            p.setUrl(properties.getProperty(URL));
            p.setDriverClassName(properties.getProperty(DRIVER_CLASS_NAME));
            p.setUsername(properties.getProperty(USERNAME));
            p.setPassword(properties.getProperty(PASSWORD));
            p.setJmxEnabled(Boolean.parseBoolean(properties.getProperty(JMX_ENABLED)));
            p.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty(TEST_WHILE_IDLE)));
            p.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty(TEST_ON_BORROW)));
            p.setValidationQuery(properties.getProperty(VALIDATION_QUERY));
            p.setTestOnReturn(Boolean.parseBoolean(properties.getProperty(TEST_ON_RETURN)));
            p.setValidationInterval(Long.parseLong(properties.getProperty(VALIDATION_INTERVAL)));
            p.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty(TIME_BETWEEN_EVICTIONRUNS_MILLIS)));
            p.setMaxActive(Integer.parseInt(properties.getProperty(MAX_ACTIVE)));
            p.setInitialSize(Integer.parseInt(properties.getProperty(INITIAL_SIZE)));
            p.setMaxWait(Integer.parseInt(properties.getProperty(MAX_WAIT)));
            p.setRemoveAbandonedTimeout(Integer.parseInt(properties.getProperty(REMOVE_ABANDONED_TIMEOUT)));
            p.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty(MIN_EVICTABLE_IDLE_TIME_MILLIS)));
            p.setMinIdle(Integer.parseInt(properties.getProperty(MIN_IDLE)));
            p.setLogAbandoned(Boolean.parseBoolean(properties.getProperty(LOG_ABANDONED)));
            p.setRemoveAbandoned(Boolean.parseBoolean(properties.getProperty(REMOVE_ABANDONED)));
            p.setJdbcInterceptors(properties.getProperty(JDBC_INTERCEPTORS));

            dataSource.setPoolProperties(p);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static ConnectionPool get() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}