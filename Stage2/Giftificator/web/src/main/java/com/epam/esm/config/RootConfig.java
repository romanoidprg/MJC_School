package com.epam.esm.config;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.errors.ErrorResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages = {"com.epam.esm.service", "com.epam.esm.web"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
public class RootConfig {
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

    @Bean()
    public ConnectionPool connectionPool(){
        return ConnectionPool.get();
    }

}