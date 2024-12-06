package com.example.decsecBackend.configuracion;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import com.example.decsecBackend.modelo.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private UsuarioServicioImpl userService;
    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users**")
                        .hasAnyAuthority(Role.ROLE_ADMIN.toString(), Role.ROLE_USER.toString())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**")
                        .hasAnyAuthority(Role.ROLE_ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/publicaciones")
                        .hasAnyAuthority(Role.ROLE_ADMIN.toString(), Role.ROLE_USER.toString())
                        .requestMatchers(HttpMethod.POST, "/api/v1/publicaciones**")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/publicaciones")
                        .hasAnyAuthority(Role.ROLE_USER.toString())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/comentarios**")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/api/v1/peticiones/**")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/api/v1/peticiones**")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
