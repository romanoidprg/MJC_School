package com.epam.esm.app;

import com.epam.esm.config.RootConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RootConfig.class)
@ComponentScan(basePackages = "com.epam.esm.controllers")
public class BootGiftApplication {

	public static void main(String[] args) {

		SpringApplication.run(BootGiftApplication.class, args);
	}
}
