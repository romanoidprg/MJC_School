package com.epam.esm.cpool;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

public enum ConnectionPool {
    REAL_DB("jdbc:mysql://localhost:3306/giftificator", "com.mysql.cj.jdbc.Driver"),
    IN_MEMORY_DB("jdbc:h2:mem:db; INIT=RUNSCRIPT FROM '~/create.sql'\\;RUNSCRIPT FROM '~/populate.sql';IGNORE_UNKNOWN_SETTINGS=TRUE;MODE=MYSQL","org.h2.Driver" );

    PoolProperties p = new PoolProperties();
    DataSource dataSource = new DataSource();

    ConnectionPool(String dbURL, String driverClassName) {
        p.setUrl(dbURL);
        p.setDriverClassName(driverClassName);
        init();
    }

    private void init() {
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
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}