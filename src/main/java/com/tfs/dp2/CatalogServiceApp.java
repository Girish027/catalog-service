package com.tfs.dp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Spring Boot application initialisation
 * Technology used : Spring JPA
 *
 * @author ram.sewak
 */
@SpringBootApplication
public class CatalogServiceApp {

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApp.class, args);
	}
}
