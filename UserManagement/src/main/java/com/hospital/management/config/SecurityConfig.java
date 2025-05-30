//package com.example.demo.config;
//
//import org.springframework.context.annotation.*;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//            	    .requestMatchers("/api/auth", "/api/auth/**").permitAll()
//            	    .anyRequest().authenticated()
//            	);
//
//        return http.build();
//        
//    }
//}
package com.hospital.management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .csrf(csrf -> csrf.disable())  // Disable CSRF protection
          .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
          .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
