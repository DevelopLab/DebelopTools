package com.app.tools.common.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author mhiraishi
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@RequestMapping(headers = "X-Requested-With=XMLHttpRequest", produces="text/plain;charset=utf-8")
public @interface AjaxMapping {

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#name()
     * @return name
     */
    @AliasFor(annotation = RequestMapping.class)
    String name() default "";

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#value()
     * @return value
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#path()
     * @return path
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#method()
     * @return method
     */
    @AliasFor(annotation = RequestMapping.class)
    RequestMethod[] method() default {};

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#params()
     * @return params
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] params() default {};

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#consumes()
     * @return consumes
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] consumes() default {};

    /**
     * @see org.springframework.web.bind.annotation.RequestMapping#produces()
     * @return produces
     */
    @AliasFor(annotation = RequestMapping.class)
    String[] produces() default {};
}