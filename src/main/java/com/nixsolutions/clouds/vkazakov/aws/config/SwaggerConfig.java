package com.nixsolutions.clouds.vkazakov.aws.config;

import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TITLE = "Users REST API";
    public static final String DESCRIPTION = "Back-end REST API.";
    public static final String VERSION = "1.0";
    public static final String TERMS_OF_SERVICE_URL = "Terms of service";
    public static final String NAME = "Victor Kazakov";
    public static final String URL = "www.nixsolutions.com";
    public static final String EMAIL = "viktor.kazakov@nixsolutions.com";
    public static final String LICENSE = "License of API";
    public static final String LICENSE_URL = "API license URL";

    private ApiInfo apiInfo() {
        return new ApiInfo(TITLE, DESCRIPTION, VERSION, TERMS_OF_SERVICE_URL,
            new Contact(NAME, URL, EMAIL), LICENSE, LICENSE_URL, Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.nixsolutions.clouds.vkazakov.aws"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(Collections.singletonList(securityContext()))
            .securitySchemes(Collections.singletonList(apiKey()));
    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
            = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections
            .singletonList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
    }

}
