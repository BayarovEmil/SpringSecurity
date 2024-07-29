package com.example.springsecurity6.security;

import com.example.springsecurity6.util.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.springsecurity6.model.Permissions.*;
import static com.example.springsecurity6.model.role.Role.ADMIN;
import static com.example.springsecurity6.model.role.Role.MANAGER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    public final JwtFilter jwtFilter;
    private final LogoutHandler logoutHandler;
    private final RateLimiter rateLimiter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req->
                                req.requestMatchers(permitSwagger).permitAll()
                                        .requestMatchers("/manager/**").hasAnyRole(ADMIN.name(), MANAGER.name())
                                        .requestMatchers(HttpMethod.GET,"/manager/**").hasAnyAuthority(ADMIN_READ.name(),MANAGER_READ.name())
                                        .requestMatchers(HttpMethod.POST,"/manager/**").hasAnyAuthority(ADMIN_CREATE.name(),MANAGER_CREATE.name())
                                        .requestMatchers(HttpMethod.PUT,"/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANAGER_UPDATE.name())
                                        .requestMatchers(HttpMethod.DELETE,"/manager/**").hasAnyAuthority(ADMIN_DELETE.name(),MANAGER_DELETE.name())

                                        .requestMatchers("/admin/**").hasRole(ADMIN.name())
                                        .requestMatchers(HttpMethod.GET,"/admin/**").hasAuthority(ADMIN_READ.name())
                                        .requestMatchers(HttpMethod.POST,"/admin/**").hasAuthority(ADMIN_CREATE.name())
                                        .requestMatchers(HttpMethod.PUT,"/admin/**").hasAuthority(ADMIN_UPDATE.name())
                                        .requestMatchers(HttpMethod.DELETE,"/admin/**").hasAuthority(ADMIN_DELETE.name())
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(rateLimiter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout->logout
                        .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()))
                        )
                ;
        return security.build();
    }

    public static String[] permitSwagger = {
            "/user/**",
            "/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
}
