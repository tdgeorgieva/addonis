package com.addonis.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket taskApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("addonis")
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/api.*"))
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Addonis API")
                .description("API for Addonis - an add-ons web registry application")
                .version("1.0")
                .build();
    }
}

