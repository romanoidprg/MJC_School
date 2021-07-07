package com.epam.esm.common_service;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomCertService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.OrderRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.common_service.impl.UserRepoService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomCertDao;
import com.epam.esm.dao.CustomTagDao;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.OrderDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderCriteria;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.epam.esm.model.User;
import com.epam.esm.model.UserCriteria;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;


@SpringBootConfiguration
public class TestConfig {

    @Bean
    public SessionFactory sessionFactory() {
        SessionFactory sf = Mockito.mock(SessionFactoryImpl.class);
        return sf;
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
        CommonDao<GiftCertificate, CertCriteria> mockCertDao = Mockito.mock(CertDao.class);
        Mockito.when(mockCertDao.create(any())).thenReturn(1L);
        Mockito.when(mockCertDao.isExist(any())).thenReturn(false);
        Mockito.when(mockCertDao.readById(anyLong())).thenReturn(new GiftCertificate());
//        Mockito.when(mockCertDao.readByCriteria(any())).thenReturn(new ArrayList<>(Arrays.asList(
//                new GiftCertificate(),
//                new GiftCertificate())));
//        Mockito.when(mockCertDao.update(any())).thenReturn(true);
//        Mockito.when(mockCertDao.deleteById(anyLong())).thenReturn(true);
        return mockCertDao;

    }

    @Bean
    public CustomCertDao customCertDao() {
        return new CertDao();
    }

    @Bean
    public CommonDao<Tag, TagCriteria> tagDao() {
        CommonDao<Tag, TagCriteria> mockTagDao = Mockito.mock(TagDao.class);
        Mockito.when(mockTagDao.create(any())).thenReturn(1L);
        Mockito.when(mockTagDao.isExist(any())).thenReturn(false);

        return mockTagDao;
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

}