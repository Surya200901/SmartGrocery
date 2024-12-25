package com.smartgrocery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

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
            	    .logoutUrl("/logout")
            	    .logoutSuccessUrl("/index")
            	    .invalidateHttpSession(true) // Invalidate the session on logout
            	    .deleteCookies("JSESSIONID") // Delete session cookie 
            	    .clearAuthentication(true)
            	    .permitAll()
            	)
            
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/updateGroceryItem/**")) // Disable CSRF for the specific endpoint
            .addFilterBefore(new CacheControlFilter(), UsernamePasswordAuthenticationFilter.class); // Add custom filter

        return http.build();
    }

    // Custom filter to set cache control headers
    public class CacheControlFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                HttpServletResponse httpResponse = (HttpServletResponse) response;

                httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private"); 
                httpResponse.setHeader("Pragma", "no-cache");
                httpResponse.setDateHeader("Expires", 0);
                httpResponse.setHeader("Cache-Control", "no-cache, no-store, no-transform"); 
                httpResponse.setHeader("ETag", UUID.randomUUID().toString()); // Generate a unique ETag 
            }
            chain.doFilter(request, response);
        }
    }
}