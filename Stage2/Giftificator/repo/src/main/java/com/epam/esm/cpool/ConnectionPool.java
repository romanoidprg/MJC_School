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

    private  static final String PROPERTIES_FILE_PATH = "resourses/db.properties";

    private DataSource dataSource = new DataSource();

    private ConnectionPool(){
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(fis);
            PoolProperties p = new PoolProperties();
            p.setDbProperties(properties);
            dataSource.setPoolProperties(p);
        } catch (IOException  e) {
            //todo logging error;
        }
    }

    public static ConnectionPool get(){
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}