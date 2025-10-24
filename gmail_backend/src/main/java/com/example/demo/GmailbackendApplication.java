package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * Main Spring Boot application entry point for the gmail backend.
 * Runs on server.port configured in application.properties.
 * Swagger UI served at /swagger-ui.html and OpenAPI JSON at /api-docs by springdoc.
 */
@SpringBootApplication
public class GmailbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmailbackendApplication.class, args);
	}

	/**
	 * PUBLIC_INTERFACE
	 * Global CORS filter to allow the React frontend to access the backend.
	 * Allows http://localhost:3000 with common methods and credentials.
	 */
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of("http://localhost:3000"));
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("*"));
		config.setExposedHeaders(List.of("Content-Disposition"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
