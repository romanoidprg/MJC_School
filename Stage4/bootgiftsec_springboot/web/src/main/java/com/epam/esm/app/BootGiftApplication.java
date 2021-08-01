package com.epam.esm.app;

import com.epam.esm.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class},
        scanBasePackages = {"com.epam.esm.*"})
@Import({SecurityConfig.class})
@EnableJpaRepositories(basePackages = {"com.epam.esm.dao"})
@EntityScan(basePackages = {"com.epam.esm.model"})
public class BootGiftApplication {

    public static void main(String[] args) {

        SpringApplication.run(BootGiftApplication.class, args);
    }
}
