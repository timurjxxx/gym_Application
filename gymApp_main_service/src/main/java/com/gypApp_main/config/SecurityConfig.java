package com.gypApp_main.config;

import com.gypApp_main.security.JWTFilter;
import com.gypApp_main.security.JWTProvider;
import com.gypApp_main.service.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JWTFilter filter;
    private final JWTProvider provider;




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/create_trainee", "/auth/create_trainer", "/auth/login").permitAll()
                        .anyRequest().authenticated()

                ).sessionManagement(sessin -> sessin.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(Customizer.withDefaults())
                .formLogin(login -> login
                        .loginPage("/auth/login"))
                .logout(logout -> logout
                        .logoutUrl("/auth/logout").permitAll()
                        .logoutSuccessHandler(logoutSuccessHandler())
                                .logoutSuccessUrl("/auth/login")
                        )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);



        return http.build();


    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        return new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, java.io.IOException {
                String token = request.getHeader("Authorization");
                log.info("Logout initiated. Token to invalidate: {}", token);

                if (token != null) {
                    provider.invalidateToken(token);
                    log.info("Token invalidated successfully.");
                } else {
                    log.info("No token found in request header.");
                }

                HttpSession session = request.getSession(false);
                if (session != null) {
                    try {
                        session.invalidate();
                        log.info("Session invalidated successfully.");
                    } catch (IllegalStateException e) {
                        log.warn("Error invalidating session: {}", e.getMessage());
                    }
                } else {
                    log.info("No session found to invalidate.");
                }

            }
        };
    }

}