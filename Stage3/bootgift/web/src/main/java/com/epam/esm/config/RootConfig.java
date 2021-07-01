package com.epam.esm.config;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.OrderRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.common_service.impl.UserRepoService;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.OrderDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = false)
@EnableTransactionManagement
public class RootConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name", ""));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) throws IOException {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.show-sql"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
//        properties.put("hibernate.current_session_context_class",
//                env.getProperty("spring.jpa.properties.hibernate.current_session_context_class"));
        properties.put("hibernate.id.new_generator_mappings", env.getProperty("spring.jpa.properties.hibernate.id.new_generator_mappings"));


        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("com.epam.esm.model");
        factoryBean.setAnnotatedClasses(GiftCertificate.class, Tag.class);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    @Autowired
    @Bean
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) throws IOException {
        return new HibernateTransactionManager(sessionFactory(dataSource()).getObject());
    }

    @Bean
    public CertRepoService certRepoService() {
        return new CertRepoService();
    }

    @Bean
    public UserRepoService userRepoService() {
        return new UserRepoService();
    }

    @Bean
    public OrderRepoService orderRepoService() {
        return new OrderRepoService();
    }

    @Bean
    public TagRepoService tagRepoService() {
        return new TagRepoService();
    }

    @Bean
    public CertDao certDao() {
        return new CertDao();
    }

    @Bean
    public TagDao tagDao() {
        return new TagDao();
    }

    @Bean
    public OrderDao orderDao() {
        return new OrderDao();
    }

    @Bean
    public UserDao userDao() {
        return new UserDao();
    }

}