package com.taskmanagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info =
        @Info(
                title = "Task Management API",
                version = "1.0",
                description = "Task related activities.",
                contact = @Contact(name = "Tiamiyu Kehinde", url = "https://task-management-dyjs.onrender.com/swagger-ui/index.html", email = "TiamiyuKehinde5@gmail.com")))
@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI customOpenAPI() {
                SecurityScheme securityScheme = new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT");

                SecurityRequirement securityRequirement = new SecurityRequirement()
                        .addList("Bearer Auth");

                return new OpenAPI()
                        .components(new Components().addSecuritySchemes("Bearer Auth", securityScheme))
                        .addSecurityItem(securityRequirement);
        }
}
