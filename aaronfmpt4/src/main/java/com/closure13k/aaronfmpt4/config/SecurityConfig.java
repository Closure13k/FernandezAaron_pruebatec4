package com.closure13k.aaronfmpt4.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PROTECTED_ENDPOINTS = {
            //Hotel and Room endpoints.
            "/agency/hotels/new",
            "/agency/hotels/newbatch",
            "/agency/hotels/edit/*",
            "/agency/hotels/delete/*",
            //Room endpoints.
            "/agency/hotels/*/rooms/new",
            "/agency/hotels/*/rooms/edit/*",
            "/agency/hotels/*/rooms/delete/*",
            //Flight endpoints.
            "/agency/flights/new",
            "/agency/flights/newbatch",
            "/agency/flights/edit/*",
            "/agency/flights/delete/*",
    };
    
    /**
     * Configures the security filter chain that carries out the security.
     * Allowing access to the protected endpoints only to authenticated users.
     * Disables CSRF and CORS protection.
     * Configures form login and HTTP basic authentication.
     *
     * @param http The HttpSecurity object to configure.
     * @return The SecurityFilterChain object that carries out the security.
     * @throws Exception If an error occurs while configuring the security.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        return http
                .authorizeHttpRequests(
                        matcherRegistry -> matcherRegistry
                                .requestMatchers(PROTECTED_ENDPOINTS).authenticated()
                                .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();
        
        
    }
}
