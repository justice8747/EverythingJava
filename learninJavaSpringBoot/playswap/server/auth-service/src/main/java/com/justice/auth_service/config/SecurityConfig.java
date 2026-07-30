package com.justice.auth_service.config;

import com.justice.auth_service.security.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Lazy
    @Autowired
    private  OAuth2AuthenticationSuccessHandler successHandler;




    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/login/**",
                                "/api/auth/**",
                                "/actuator/health",
                                "/internal/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authenticationSuccessHandler(successHandler)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenConverter(exchange -> {
                            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                                return Mono.just(new BearerTokenAuthenticationToken(authHeader.substring(7)));
                            }
                            return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("playswap_token"))
                                    .map(HttpCookie::getValue)
                                    .map(BearerTokenAuthenticationToken::new);
                        })
                        .jwt(org.springframework.security.config.Customizer.withDefaults())
                )
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }
    @Bean
    public ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultReactiveOAuth2UserService service = new DefaultReactiveOAuth2UserService();
        return service;
    }

    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("AUTH_SESSION");
        resolver.addCookieInitializer(builder -> builder.path("/").sameSite("Lax"));
        return resolver;
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}