package com.mariuszgajewski.rewards.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/rewards-service/**")
                .authenticated()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs.yaml").permitAll()
                .anyRequest()
                .authenticated())
                .csrf().disable()
                .httpBasic();
        return http.build();
    }
}
