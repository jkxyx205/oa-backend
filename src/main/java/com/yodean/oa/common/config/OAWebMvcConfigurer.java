package com.yodean.oa.common.config;

import com.yodean.oa.common.interceptor.TestInterceptor;
import com.yodean.oa.sys.dictionary.core.StringToWordConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by rick on 2018/3/16.
 */
@Configuration
public class OAWebMvcConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TestInterceptor()).addPathPatterns("/tpl/index");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }


    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToWordConverter());
    }

}
