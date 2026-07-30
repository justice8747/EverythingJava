package com.justice.microservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI productServiceApi(){
        return new OpenAPI()
                .info(new Info().title("Product Service Api")
                        .description("This Is The REST API For Product Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("You can refer To The Product Service Wiki Documentation")
                        .url("https://product-service-dummy-url.comd/docs"));
    }
}
