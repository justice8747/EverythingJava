package com.justice.api_gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

@SpringBootTest
class ApiGatewayApplicationTests {

	@Autowired
	private Environment env;

	@Autowired(required = false)
	private GatewayProperties gatewayProperties;

	@Autowired(required = false)
	private RouteLocator routeLocator;

	@Test
	void contextLoads() {
		System.out.println("====== ENVIRONMENT KEYS ======");
		if (env instanceof AbstractEnvironment) {
			for (PropertySource<?> propertySource : ((AbstractEnvironment) env).getPropertySources()) {
				if (propertySource instanceof EnumerablePropertySource) {
					String[] propertyNames = ((EnumerablePropertySource<?>) propertySource).getPropertyNames();
					for (String propertyName : propertyNames) {
						if (propertyName.startsWith("spring.cloud.gateway")) {
							System.out.println(propertySource.getName() + " -> " + propertyName + " = " + env.getProperty(propertyName));
						}
					}
				}
			}
		}
		System.out.println("=============================");

		System.out.println("====== GATEWAY PROPERTIES ======");
		if (gatewayProperties != null) {
			System.out.println("GatewayProperties is NOT null.");
			System.out.println("Routes size: " + gatewayProperties.getRoutes().size());
			gatewayProperties.getRoutes().forEach(route -> {
				System.out.println("  Route ID: " + route.getId());
				System.out.println("    URI: " + route.getUri());
				System.out.println("    Predicates: " + route.getPredicates());
				System.out.println("    Filters: " + route.getFilters());
			});
		} else {
			System.out.println("GatewayProperties is NULL!");
		}
		System.out.println("================================");

		System.out.println("====== RUNTIME ROUTELOCATOR ======");
		if (routeLocator != null) {
			System.out.println("RouteLocator is NOT null.");
			try {
				routeLocator.getRoutes().collectList().block().forEach(route -> {
					System.out.println("  Runtime Route ID: " + route.getId());
					System.out.println("    URI: " + route.getUri());
					System.out.println("    Order: " + route.getOrder());
					System.out.println("    Predicate: " + route.getPredicate());
				});
			} catch (Exception e) {
				System.out.println("Error reading routes: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("RouteLocator is NULL!");
		}
		System.out.println("==================================");
	}

}
