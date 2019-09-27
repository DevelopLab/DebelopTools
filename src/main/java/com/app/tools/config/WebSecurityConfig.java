package com.app.tools.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * WebAppリソースに対して、認証・認可を管理する。
 *
 * @author m.hiraishi
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

	private static final String[] WEBAPP_RESOURCES = { "/css/**", "/js/**", "/img/**", "/fonts/**", "/*/**" };

	private static final String[] PERMIT_ALL_RESOURCES = { "/login", "*error*", "/login/authenticate", "/list", "/list/*" ,"/entry" ,"/entry/*" };

	/**
	 * WebSecurityの設定.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		LOGGER.info("***** WebSecurityの設定を行います。 *****");
		// 静的リソース(img、css、fonts、js)に対するアクセスはセキュリティ設定を無視する
		web.ignoring().antMatchers(WEBAPP_RESOURCES);
	}

	/**
	 * HttpSecurityの設定.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.info("***** HttpSecurityの設定を行います。 *****");
		http.csrf().disable();
		// 認可の設定
		http.authorizeRequests()
		    // 認証無しでアクセスできるURLを設定
//			.antMatchers(HttpMethod.GET, PERMIT_ALL_RESOURCES).permitAll()
//			.antMatchers(HttpMethod.POST, PERMIT_ALL_RESOURCES).permitAll()
			.antMatchers(WEBAPP_RESOURCES).permitAll();

			// 上記以外は認証が必要にする設定
//			.anyRequest().authenticated();

//		// ログイン設定
//		http.formLogin()
//			.loginPage("/login")
//			.loginProcessingUrl("/login/authenticate")
//			.defaultSuccessUrl("/list", true)
//			.usernameParameter("loginId").passwordParameter("password");
//
//		// ログアウト設定
//		http.logout()
//			.logoutUrl("/logout")
//			.logoutSuccessUrl("/login")
//			.invalidateHttpSession(true);
	}
}