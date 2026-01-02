package com.capitole.ecommerce.pricing.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * Provides interactive API documentation accessible at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pricingServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Santiago De Pol");
        contact.setEmail("sdepolp@gmail.com");

        License license = new License()
                .name("Capitole Technical Assessment")
                .url("https://github.com/yourusername/pricing-service");

        Info info = new Info()
                .title("Pricing Service API")
                .version("1.0.0")
                .description("E-commerce pricing service providing REST API endpoints to query applicable prices based on date, product, and brand criteria. " +
                        "Built with hexagonal architecture following clean code principles.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}