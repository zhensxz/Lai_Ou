package com.example.Backend.config;

import com.example.Backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public JwtUtil jwtUtil(@Value("${app.jwt.secret}") String base64Secret,
                           @Value("${app.jwt.access-ttl-ms:1800000}") long ttl) {
        return new JwtUtil(base64Secret, ttl);
    }
}


