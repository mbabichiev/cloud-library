package com.gmail.foy.maxach.cloudlibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "Cloud Library Documentation",
                "In this documentation you can find endpoints, required params and etc. " +
                    "Authorization is based on Bearer tokens",
                "0.0.1",
                "",
                new Contact(
                        "Support",
                        "http://t.me/stevenfoy",
                        "maxach.foy@gmail.com"
                ),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.html",
                Collections.emptyList());
    }


    private ApiKey getApiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }


    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }


    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gmail.foy.maxach.cloudlibrary"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
                //.securityContexts(Arrays.asList(securityContext()))
                //.securitySchemes(Arrays.asList(getApiKey()));
    }
}

