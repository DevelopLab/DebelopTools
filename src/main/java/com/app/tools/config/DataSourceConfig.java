package com.app.tools.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

/**
 *
 * @author mhiraishi
 *
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
public class DataSourceConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

	@Autowired
	private Environment env;

	/**
	 * サーバ起動時にトランザクション管理の設定を行う。
	 *
	 * @return dataSourceTransactionManager
	 */
	@Bean
	public DataSourceTransactionManager transactionManager() {
		LOGGER.info("***** DBへの接続設定を行います。 *****");
		return new DataSourceTransactionManager(dataSource());
	}

	/**
	 * DataSourceの設定を行う。
	 *
	 * @return hikariDataSource
	 */
	@Bean
	public DataSource dataSource() {
		 final HikariDataSource ds = new HikariDataSource();
	        ds.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	        ds.setJdbcUrl(env.getProperty("jdbc.url"));
	        ds.setUsername(env.getProperty("jdbc.username"));
	        ds.setPassword(env.getProperty("jdbc.password"));
	        ds.setMaximumPoolSize(env.getProperty("jdbc.pool.size.max", Integer.class));
	        return ds;
	}
}
