package com.sasken.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BlogCraft Publishing Workflow API")
                        .description("A comprehensive REST API for managing blog posts, reviews, and publishing workflow. " +
                                "This API provides endpoints for creating, updating, reviewing, and publishing blog content " +
                                "with role-based access control and status management.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Sasken Team")
                                .email("support@blogcraft.com")
                                .url("https://blogcraft.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.blogcraft.com")
                                .description("Production Server")
                ));
    }
} 