package com.kimlongdev.shopme.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(managerment -> managerment.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS // No session will be created
        )).authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/products/*/review").permitAll() // Allow public access to product reviews
                        .requestMatchers("/api/**").authenticated() // Secure all /api/** endpoints
                        .anyRequest().permitAll()
        ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConnfigurationSource()));

        return http.build();
    }

    // CORS configuration
    private CorsConfigurationSource corsConnfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("*")); // Allow all domains
                config.setAllowedMethods(Collections.singletonList("*")); // Allow all HTTP methods
                config.setAllowedHeaders(Collections.singletonList("*")); // Allow all headers
                config.setAllowCredentials(true ); // Allow sending cookies
                config.setExposedHeaders(Collections.singletonList("Authorization")); // Frontend can read the Authorization header
                config.setMaxAge(3600L); // Cache preflight response for 1 hour

                return config;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
