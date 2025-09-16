package com.example.social_network_backend.Config;

import com.example.social_network_backend.Repositories.UserRepository;
import com.example.social_network_backend.token.TokenRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.example.social_network_backend.Entities.Permission.*;
import static com.example.social_network_backend.Entities.UserRole.ADMIN;
import static com.example.social_network_backend.Entities.UserRole.USER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository repository;
    private final LogoutHandler logoutHandler;

    @Bean
    public JwtAuthenticationFilter jwtAuthFilter(JwtService jwtService,
                                                 UserDetailsService userDetailsService,
                                                 TokenRepository tokenRepository) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService, tokenRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (org.springframework.security.core.userdetails.UserDetails) repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new ApplicationAuditAware();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh-token",
            "/api/v1/auth"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/api/v1/users/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/users").hasAnyAuthority(ADMIN_READ.name())
                        .requestMatchers(GET, "/api/v1/users/*").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/api/v1/users/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/users/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/users/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/users/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers("/api/v1/auth/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/auth/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/api/v1/auth/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/auth/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/auth/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/auth/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers("/api/v1/comments/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/comments").hasAnyAuthority(ADMIN_UPDATE.name(), USER_READ.name())
                        .requestMatchers(GET, "/api/v1/comments/*").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(POST, "/api/v1/comments/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(POST, "/api/v1/comments/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/comments/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/comments/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/comments/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers("/api/v1/likes/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/likes").hasAnyAuthority(ADMIN_READ.name())
                        .requestMatchers(GET, "/api/v1/likes/*").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/api/v1/likes/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/likes/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/likes/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/likes/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers("/api/v1/messages/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/messages").hasAnyAuthority(ADMIN_READ.name())
                        .requestMatchers(GET, "/api/v1/messages/*").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/api/v1/messages/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/messages/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/messages/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/messages/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers("/api/v1/posts/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/posts/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(GET, "/api/v1/posts").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/api/v1/posts/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/posts/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/posts/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/posts/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers("/api/v1/subscriptions/**").hasAnyRole(ADMIN.name(), USER.name())
                        .requestMatchers(GET, "/api/v1/subscriptions/**").hasAnyAuthority(ADMIN_READ.name(), USER_READ.name())
                        .requestMatchers(POST, "/api/v1/subscriptions/**").hasAnyAuthority(ADMIN_CREATE.name(), USER_CREATE.name())
                        .requestMatchers(PUT, "/api/v1/subscriptions/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(DELETE, "/api/v1/subscriptions/**").hasAnyAuthority(ADMIN_DELETE.name(), USER_DELETE.name())
                        .requestMatchers(PATCH, "/api/v1/subscriptions/**").hasAnyAuthority(ADMIN_UPDATE.name(), USER_UPDATE.name())
                        .requestMatchers(GET, "/api/v1/statistics").hasAnyAuthority(ADMIN_READ.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 401 Unauthorized
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // 403 Forbidden
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden\"}");
                        })
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, auth) -> SecurityContextHolder.clearContext())
                );
        return http.build();
    }
}