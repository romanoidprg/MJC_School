package com.epam.esm.config;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomCertService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.OrderRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.common_service.impl.UserRepoService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomCertDao;
import com.epam.esm.dao.CustomTagDao;
import com.epam.esm.dao.CustomUserDao;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.OrderDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.model.*;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
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
    public CommonService<GiftCertificate> certRepoService() {
        return new CertRepoService();
    }

    @Bean
    public CustomCertService customCertRepoService() {
        return new CertRepoService();
    }

    @Bean
    public CommonService<User> userRepoService() {
        return new UserRepoService();
    }

    @Bean
    public CommonService<Order> orderRepoService() {
        return new OrderRepoService();
    }

    @Bean
    public CommonService<Tag> tagRepoService() {
        return new TagRepoService();
    }

    @Bean
    public CustomTagServise<Tag> customTagRepoService() {
        return new TagRepoService();
    }

    @Bean
    public CommonDao<GiftCertificate, CertCriteria> certDao() {
        return new CertDao();
    }

    @Bean
    public CustomCertDao customCertDao() {
        return new CertDao();
    }


    @Bean
    public CommonDao<Tag, TagCriteria> tagDao() {
        return new TagDao();
    }

    @Bean
    public CustomTagDao<Tag> customTagDao() {
        return new TagDao();
    }

    @Bean
    public CommonDao<Order, OrderCriteria> orderDao() {
        return new OrderDao();
    }

    @Bean
    public CommonDao<User, UserCriteria> userDao() {
        return new UserDao();
    }

    @Bean
    CustomUserDao customUserDao() {
        return new UserDao();
    }

    @Bean
    CustomUserService customUserService() {
        return new UserRepoService();
    }

}