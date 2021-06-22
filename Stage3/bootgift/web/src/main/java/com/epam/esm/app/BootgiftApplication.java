package com.epam.esm.app;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.config.RootConfig;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.dao.impl.TagDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(RootConfig.class)
public class BootgiftApplication {

	public static void main(String[] args) {

		SpringApplication.run(BootgiftApplication.class, args);
	}
//
//	@Bean
//	public CertRepoService certRepoService() {
//		return new CertRepoService();
//	}
//
//	@Bean
//	public TagRepoService tagRepoService() {
//		return new TagRepoService();
//	}
//
//	@Bean
//	public CertDao certDao() {
//		return new CertDao();
//	}
//
//	@Bean
//	public TagDao tagDao() {
//		return new TagDao();
//	}
//
//	@Bean()
//	public ConnectionPool connectionPool(){
//		return ConnectionPool.get();
//	}
}
