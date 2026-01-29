package com.carpooling.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.carpooling.repository.UserRepository;

/**
 * Spring Security Configuration
 * Handles authentication, authorization, and session management
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Password encoder using BCrypt
     * BCrypt automatically handles salt generation and is secure against rainbow tables
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * UserDetailsService implementation
     * Loads user by username for authentication
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().toString())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    /**
     * Authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Security filter chain
     * Configures:
     * - Which endpoints require authentication
     * - CORS policy
     * - Session management
     * - HTTP Basic (for testing via Postman)
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS configuration
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOriginPatterns(java.util.List.of("*"));
                    corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(java.util.List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))

                // CSRF protection (disabled for stateless APIs, kept for session-based for safety)
                .csrf(csrf -> csrf.disable())

                // Authorization rules
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/**").authenticated()

                        // Ride endpoints
                        .requestMatchers("GET", "/api/rides/**").permitAll()
                        .requestMatchers("POST", "/api/rides").hasRole("DRIVER")
                        .requestMatchers("GET", "/api/rides/my-rides").hasRole("DRIVER")

                        // Booking endpoints
                        .requestMatchers("POST", "/api/bookings").hasRole("PASSENGER")
                        .requestMatchers("GET", "/api/bookings/**").authenticated()
                        .requestMatchers("PUT", "/api/bookings/**").hasRole("DRIVER")
                        .requestMatchers("DELETE", "/api/bookings/**").authenticated()

                        // H2 Console (for development only - remove in production)
                        .requestMatchers("/h2-console/**").permitAll()

                        // All other requests require authentication
                        .anyRequest().authenticated()
                )

                // Session management (stateful - session-based auth)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixationProtection(org.springframework.security.config.Customizer.withDefaults())
                )

                // HTTP Basic (for testing)
                .httpBasic(basic -> basic.realmName("Car Pooling API"))

                // Authentication provider
                .authenticationProvider(authenticationProvider());

        // H2 Console headers (development only)
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
