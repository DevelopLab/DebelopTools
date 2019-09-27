package com.app.tools.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@ComponentScan("com.app.tools")
@SpringBootApplication
public class BootApplication implements WebMvcConfigurer {

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(BootApplication.class, args);
    }

    /**
     * 静的リソースのロケーションパス設定を行う。
     *
     */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("css/**").addResourceLocations("classpath:/templates/css/");
		registry.addResourceHandler("fonts/**").addResourceLocations("classpath:/templates/fonts/");
		registry.addResourceHandler("img/**").addResourceLocations("classpath:/templates/img/");
		registry.addResourceHandler("js/**").addResourceLocations("classpath:/templates/js/");
	}
}
