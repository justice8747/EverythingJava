package com.justice.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpCookie;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/api/auth/**",
                                "/oauth2/**",
                                "/login/**",
                                "/actuator/health"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .bearerTokenConverter(exchange -> {
                            // Read JWT from cookie instead of Authorization header
                            HttpCookie cookie = exchange.getRequest()
                                    .getCookies()
                                    .getFirst("playswap_token");
                            if (cookie != null) {
                                return Mono.just(new BearerTokenAuthenticationToken(
                                        cookie.getValue()));
                            }
                            return Mono.empty();
                        })
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public org.springframework.security.oauth2.server.resource.authentication
            .ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        return new org.springframework.security.oauth2.server.resource
                .authentication.ReactiveJwtAuthenticationConverter();
    }
}
