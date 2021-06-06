package com.epam.esm.cpool;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

public enum ConnectionPool {
    POOL(new PoolProperties(), new DataSource());

    PoolProperties p;
    DataSource dataSource;

    ConnectionPool(PoolProperties poolProperties, DataSource dataSource) {
        p = poolProperties;
        this.dataSource = dataSource;
        init();
    }

    private void init() {
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
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}