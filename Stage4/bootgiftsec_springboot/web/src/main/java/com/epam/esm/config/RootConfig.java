package com.epam.esm.config;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomCertService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.OrderRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.common_service.impl.UserRepoService;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.epam.esm.*"})
@ComponentScan(basePackages = {"com.epam.esm.*"})
@EntityScan(basePackages = {"com.epam.esm.*"})
public class RootConfig {

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
    CustomUserService customUserService() {
        return new UserRepoService();
    }

}