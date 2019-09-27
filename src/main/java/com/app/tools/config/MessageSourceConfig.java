package com.app.tools.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * MessageSourceの設定を行う。
 *
 */
@Configuration
public class MessageSourceConfig {

	/**
	 * MessageSourceの設定を行う。
	 *
	 * @return messageSource
	 */
	@Bean(name = "messageSource")
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

	/**
	 * LocaleResolverの設定を行う。
	 *
	 * @return resolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver resolver = new SessionLocaleResolver();
	    resolver.setDefaultLocale(Locale.JAPAN);
	    return resolver;
	}

	/**
	 * MessageSourceAccessorの設定を行う。
	 *
	 * @param messageSource
	 * @return MessageSourceAccessor
	 */
	@Bean(name = "messageSourceAccessor")
	public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource){
	    return new MessageSourceAccessor(messageSource, Locale.JAPAN);
	}
}
