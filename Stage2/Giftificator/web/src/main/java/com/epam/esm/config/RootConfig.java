package com.epam.esm.config;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(basePackages={"com.epam.esm.service","com.epam.esm.repo","com.epam.esm.web"},
        excludeFilters={
                @ComponentScan.Filter(type= FilterType.ANNOTATION, value= EnableWebMvc.class)
        })
public class RootConfig {
        @Bean
        public CommonDao<GiftCertificate, CertCriteria> certDao (){
                return new CertDao();
        }
        @Bean
        public CommonDao<GiftCertificate, CertCriteria> tagDao (){
                return new CertDao();
        }
        @Bean
        public CommonService<GiftCertificate> certRepoService (){
                return new CertRepoService();
        }
}
