package com.app.tools.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author mhiraishi
 *
 */
@Component
public class ExceptionHandler implements HandlerExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

	/**
	 * 補足されない例外が発生した際にエラー画面に遷移させる。
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		LOGGER.error("補足されない例外が発生しました。 exception={}", ex);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");

		return modelAndView;
	}

}
