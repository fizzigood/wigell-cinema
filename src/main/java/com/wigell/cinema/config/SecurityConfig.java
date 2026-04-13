package com.wigell.cinema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

// Säkerhetskonfiguration med rollbaserad åtkomstkontroll via Keycloak JWT.
// ADMIN: hanterar kunder, filmer, lokaler och föreställningar.
// USER: bokar salonger och köper biljetter.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                
                .requestMatchers("/actuator/**").permitAll()
                
                .requestMatchers("/api/v1/customers/**").hasRole("ADMIN")
                    
                .requestMatchers("/api/v1/rooms/**").hasRole("ADMIN")
                    
                .requestMatchers(HttpMethod.GET, "/api/v1/movies", "/api/v1/movies/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/movies").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/movies/**").hasRole("ADMIN")
                    
                .requestMatchers(HttpMethod.GET, "/api/v1/screenings", "/api/v1/screenings/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/screenings").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/screenings/**").hasRole("ADMIN")
                    
                .requestMatchers("/api/v1/bookings/**").hasAnyRole("USER", "ADMIN")
                    
                .requestMatchers("/api/v1/tickets/**").hasAnyRole("USER", "ADMIN")
                    
                .anyRequest().authenticated()
            )
            // Validering mot Keycloak
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    // Mappar Keycloaks roller till Spring Security
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        return converter;
    }

}