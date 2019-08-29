package com.github.cuinipeng.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author cuinipeng@163.com
 * @date 2019/8/29 21:44
 * @description Swagger 配置, Springfox 提供一个 Docket 对象, 可以配置 Swagger 的各项属性
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket() {

        ApiInfo apiInfo = new ApiInfo(
            "Spring Boot 项目集成 Swagger 实例文档",
            "https://github.com/cuinipeng",
            "API v1.0",
            "Apache 2.0",
            new Contact("cuinipeng", "https://github.com/cuinipeng", "cuinipeng@163.com"),
            "Apache",
            "http://www.apache.org/",
            Collections.EMPTY_LIST
        );

        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo);
    }
}
