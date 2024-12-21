package com.smartgrocery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

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
                		"/updateGroceryItem/{id}",
                		"/logout",
                		"/profile",
                		"/editprofile",
                		"/static/**", "/css/**", "/js/**", "/images/**").permitAll() 
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/groceryList", true)
                .permitAll()
            )
            .logout(logout -> logout
                    .logoutUrl("/logout") // Ensure this matches your frontend request
                    .logoutSuccessUrl("/index")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/updateGroceryItem/**")); // Disable CSRF for the specific endpoint

        return http.build();
    }
}