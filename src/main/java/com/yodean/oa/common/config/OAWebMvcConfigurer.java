package com.yodean.oa.common.config;

import com.yodean.oa.common.interceptor.TestInterceptor;
import org.springframework.context.annotation.Configuration;
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

//    @Bean
//    public ServletWebServerFactory servletContainer() {
//        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        tomcat.addAdditionalTomcatConnectors(createConnector());
//        return tomcat;
//    }
//
//    private Connector createConnector() {
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
//        try {
//            connector.setScheme("http");
//            connector.setPort(8080);
//            protocol.setMaxSwallowSize(-1); //解决
//            return connector;
//        }
//        catch (Exception ex) {
//            throw new IllegalStateException("can't access keystore: [" + "keystore"
//                    + "] or truststore: [" + "keystore" + "]", ex);
//        }
//    }


}
