package com.growin.marvel.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class SwaggerBeanConfig {



    @Bean
    public GroupedOpenApi publicApi() {

        return GroupedOpenApi.builder()
                .group("")
                .packagesToScan("com.growin.marvel.api.controller")
                .pathsToMatch("/*")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Marvel Api")
                        .description("Coding challenge"));
    }
}
