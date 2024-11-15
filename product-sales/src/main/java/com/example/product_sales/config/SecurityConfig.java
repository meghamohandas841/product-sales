package com.example.product_sales.config;


import com.example.product_sales.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    String username = System.getenv("ADMIN_USER");
    String password = System.getenv("ADMIN_PASSWORD");

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password must be provided");
        }

        String encodedPassword = passwordEncoder().encode(password);

        UserDetails user = User.withUsername(username)
                .password(encodedPassword)
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, AppConstants.PRODUCT_API_URL,AppConstants.SALES_API_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, AppConstants.PRODUCT_API_URL,AppConstants.SALES_API_URL).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, AppConstants.PRODUCT_API_URL,AppConstants.SALES_API_URL).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, AppConstants.PRODUCT_API_URL,AppConstants.SALES_API_URL).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic();

        return http.build();
    }
}