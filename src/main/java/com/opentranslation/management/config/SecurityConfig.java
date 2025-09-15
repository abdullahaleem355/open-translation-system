package com.opentranslation.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.opentranslation.management.security.JwtAuthenticationFilter;

/**
 * Security configuration class for the Translation Management System.
 * <p>
 * Configures JWT-based authentication, disables session management, and sets up the security filter chain for incoming requests.
 * </p>
 */
@Configuration
public class SecurityConfig
{

  /**
   * JWT authentication filter that validates incoming requests for a valid JWT token.
   */
  private final JwtAuthenticationFilter jwtFilter;

  /**
   * Constructs the SecurityConfig with the provided JWT authentication filter.
   *
   * @param jwtFilter the JWT authentication filter to be added to the filter chain
   */
  public SecurityConfig(JwtAuthenticationFilter jwtFilter)
  {
    this.jwtFilter = jwtFilter;
  }

  /**
   * Configures the Spring Security filter chain.
   * <p>
   * Disables CSRF, sets the session management policy to stateless, allows token generation endpoint without authentication, and requires JWT for all other
   * endpoints.
   * </p>
   *
   * @param http the {@link HttpSecurity} object to configure
   * @return the configured {@link SecurityFilterChain}
   * @throws Exception if any configuration error occurs
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
  {
    http.csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/token")
                                           .permitAll()  // allow token generation without auth
                                           .anyRequest()
                                           .authenticated()                     // everything else requires JWT
        );

    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Provides the {@link AuthenticationManager} bean required by Spring Security.
   *
   * @param config the {@link AuthenticationConfiguration} used to retrieve the authentication manager
   * @return the {@link AuthenticationManager} instance
   * @throws Exception if there is an error retrieving the authentication manager
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
  {
    return config.getAuthenticationManager();
  }
}
