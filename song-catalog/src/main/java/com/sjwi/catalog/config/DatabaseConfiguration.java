package com.sjwi.catalog.config;

import java.io.FileNotFoundException;
import java.util.Map;

import javax.sql.DataSource;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.sjwi.catalog.model.SqlQueryStore;

@Configuration
public class DatabaseConfiguration {

	@Value("${spring.datasource.driverClassName}")
	String dbClass;

	@Bean
	public Map<String,String> queryStore() throws FileNotFoundException{
		return new SqlQueryStore(dbClass).getQueries();
	}
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
	@Bean
	 public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
	    return new NamedParameterJdbcTemplate(dataSource);
	 }
	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ConditionalOnProperty(value="spring.datasource.driver-class-name", havingValue="org.h2.Driver", matchIfMissing=false)
	public ServletRegistrationBean h2servletRegistration() {
	    ServletRegistrationBean registration = new ServletRegistrationBean(new WebServlet());
	    registration.addUrlMappings("/console/*");
	    return registration;
	}
}
