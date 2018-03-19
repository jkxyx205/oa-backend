package com.yodean.oa.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by rick on 2018/3/19.
 */
@Configuration
@EnableSwagger2
public class Swagger2Configurer {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yodean.oa"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("项目OA: Spring Boot中使用Swagger2构建RESTful APIs")
                .description("有问题欢迎联系我")
                .termsOfServiceUrl("http://xhope.top")
                .contact(new Contact("Rick.xu", "http://xhope.top", "jkxyx205@163.com"))
                .version("1.0")
                .build();
    }
}
