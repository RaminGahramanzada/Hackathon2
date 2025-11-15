package com.easyfin.openbanking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS configuration to allow iOS app to connect to the API
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // Allow all origins for development (ngrok and cloud deployment compatibility)
        corsConfiguration.setAllowCredentials(false); // Must be false when using "*"
        corsConfiguration.addAllowedOrigin("*");
        
        // Allow ALL headers (critical for ngrok and iOS compatibility)
        corsConfiguration.addAllowedHeader("*");
        
        // Expose common headers
        corsConfiguration.setExposedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));
        
        // Allow all methods
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        
        // Set max age for preflight requests
        corsConfiguration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}

