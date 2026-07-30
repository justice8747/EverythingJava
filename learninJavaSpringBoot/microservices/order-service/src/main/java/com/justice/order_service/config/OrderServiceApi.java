package com.justice.order_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceApi {
    @Bean
    public OpenAPI productServiceApi(){
        return new OpenAPI()
                .info(new Info().title("Order Service Api")
                        .description("This Is The REST API For Order Service")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("You can refer To The Order Service Wiki Documentation")
                        .url("https://order-service-dummy-url.comd/docs"));
    }
}

