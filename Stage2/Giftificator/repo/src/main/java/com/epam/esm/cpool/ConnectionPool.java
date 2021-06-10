package com.epam.esm.cpool;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class ConnectionPool {

    private static final ConnectionPool INSTANCE = new ConnectionPool();

    private  static final String PROPERTIES_FILE_PATH = "resources/db.properties";

    private DataSource dataSource = new DataSource();

    private ConnectionPool(){
//        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH)) {
//            Properties properties = new Properties();
//            properties.load(fis);
            PoolProperties p = new PoolProperties();
//            p.setDbProperties(properties);
            p.setUrl("jdbc:mysql://localhost:3306/giftificator");
            p.setDriverClassName("com.mysql.cj.jdbc.Driver");
            p.setUsername("root");
            p.setPassword("romanoid");
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(100);
            p.setInitialSize(10);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(10);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors(
                    "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;" +
                            "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            dataSource.setPoolProperties(p);
            dataSource.setPoolProperties(p);
//        } catch (IOException  e) {
//            todo logging error;
//        }
    }

    public static ConnectionPool get(){
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}