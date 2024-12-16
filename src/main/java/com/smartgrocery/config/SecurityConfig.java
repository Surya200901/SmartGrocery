package com.smartgrocery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCryptPasswordEncoder to handle password encoding
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/groceryList", 
                		"/register", "/login", "/error", 
                		"/index",
                		"/css/**", "/js/**",
                		"/index",
                		"/grocery",
                		"/updateGroceryItem/**",
                		"/updateGroceryItem/{id}").permitAll() 
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/groceryList", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/index")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.sendRedirect("/login"); // Redirect to login if user is not authenticated
                    })
                )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/updateGroceryItem/**")); // Disable CSRF for the specific endpoint

        return http.build();
    }
}