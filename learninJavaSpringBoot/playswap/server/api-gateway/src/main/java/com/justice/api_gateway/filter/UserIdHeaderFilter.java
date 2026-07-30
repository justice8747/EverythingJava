package com.justice.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserIdHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    var authentication = securityContext.getAuthentication();

                    if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                        Jwt jwt = jwtAuth.getToken();
                        String preferredUsername = jwt.getClaimAsString("preferred_username");
                        String userId = (preferredUsername != null && !preferredUsername.isBlank())
                                ? preferredUsername
                                : jwt.getSubject();

                        log.debug("Forwarding request with X-User-Id: {}", userId);

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                            private HttpHeaders cachedHeaders;

                            @Override
                            public HttpHeaders getHeaders() {
                                if (this.cachedHeaders == null) {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.putAll(getDelegate().getHeaders());
                                    headers.set("X-User-Id", userId);
                                    this.cachedHeaders = HttpHeaders.readOnlyHttpHeaders(headers);
                                }
                                return this.cachedHeaders;
                            }
                        };

                        ServerWebExchange modifiedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(modifiedExchange);
                    }

                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}