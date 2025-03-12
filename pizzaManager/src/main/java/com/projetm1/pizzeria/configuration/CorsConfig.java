package com.projetm1.pizzeria.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configuration pour toutes les routes
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8080", "http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);

                // Configuration spécifique pour les API publiques
                registry.addMapping("/api/public/**")
                        .allowedOrigins("*") // Autorise toutes les origines pour cette route
                        .allowedMethods("GET")
                        .allowedHeaders("Content-Type");

                // Configuration pour une API spécifique
                registry.addMapping("/api/secure/**")
                        .allowedOrigins("http://secure.example.com")
                        .allowedMethods("POST", "PUT")
                        .allowedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true);
            }
        };
    }
}
