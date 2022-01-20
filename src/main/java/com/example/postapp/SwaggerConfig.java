package com.example.postapp;


import com.example.postapp.controllers.post.PostController;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi postApi() {
        return GroupedOpenApi.builder().group("Post API").pathsToMatch("/api/posts/**").build();
    }

    @Bean
    public GroupedOpenApi favoriteApi() {
        return GroupedOpenApi.builder().group("Favorite API").pathsToMatch("/api/favorites/**").build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("Auth API").pathsToMatch("/api/auth/**").build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat(
                                        "JWT").description("JWT認証が必要です。Request headerに以下の形式でトークンを設定。<br " +
                                        "/>Authorization:" +
                                        " " +
                                        "Bearer " +
                                        "token_value")));
    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> operation.addParametersItem(
                new Parameter()
                        .in("header")
                        .required(true)
                        .description("myCustomHeader")
                        .name("myCustomHeader"));
    }
}