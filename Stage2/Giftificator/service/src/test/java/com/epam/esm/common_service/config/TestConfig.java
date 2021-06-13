package com.epam.esm.common_service.config;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

@Configuration
public class TestConfig {
    @Bean
    public CertRepoService certRepoService() {
        return new CertRepoService();
    }

    @Bean
    public TagRepoService tagRepoService() {
        return new TagRepoService();
    }

    @Bean
    public CertDao certDao() {
        CertDao mockCertDao = Mockito.mock(CertDao.class);
        Mockito.when(mockCertDao.create(any())).thenReturn(true);
        Mockito.when(mockCertDao.readById(anyLong())).thenReturn(new GiftCertificate());
        Mockito.when(mockCertDao.readByCriteria(any())).thenReturn(new ArrayList<>(Arrays.asList(
                new GiftCertificate(),
                new GiftCertificate())));
        Mockito.when(mockCertDao.update(any())).thenReturn(true);
        Mockito.when(mockCertDao.deleteById(anyLong())).thenReturn(true);
        return mockCertDao;
    }

    @Bean
    public TagDao tagDao() {
        return new TagDao();
    }

    @Bean()
    public ConnectionPool connectionPool(){
        return ConnectionPool.get();
    }
}