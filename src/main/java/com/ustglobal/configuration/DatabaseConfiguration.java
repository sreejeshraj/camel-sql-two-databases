package com.ustglobal.configuration;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseConfiguration {

	@Primary
	@Bean(name = "primaryDataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	//There should be only one primary DataSource
	@Bean(name = "secondaryDataSource")
	@ConfigurationProperties(prefix = "secondary.datasource")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}

}
