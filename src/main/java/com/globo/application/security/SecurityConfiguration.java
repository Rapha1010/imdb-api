package com.globo.application.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/auth/signin",
            "/movies"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/auth/signin").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movies").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","/documentacao-api/**", "/api-docs/**").permitAll()

                        // Endpoints protegidos para usuários autenticados
                        .requestMatchers(HttpMethod.PUT, "/users/self-edit").authenticated()
                        .requestMatchers(HttpMethod.POST, "/movies/vote/{movieId}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/my-account").authenticated()

                        // Endpoints exclusivos para ADMIN
                        .requestMatchers(HttpMethod.POST, "/auth/register").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.POST, "/movies/").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.PUT, "/movies/{id}").hasRole("ADMINISTRATOR")
                        .requestMatchers(HttpMethod.DELETE, "/movies/{id}").hasRole("ADMINISTRATOR")

                        // Negar qualquer outra requisição
                        .anyRequest().denyAll()
                )
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
