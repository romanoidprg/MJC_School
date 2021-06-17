package com.epam.esm.common_service.config;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;

@Configuration
public class EmbededDbTestConfig {
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
        return new CertDao();
    }

    @Bean
    public TagDao tagDao() {
        return new TagDao();
    }

    @Bean
    public ConnectionPool connectionPool(){
        return ConnectionPool.get();
    }
}