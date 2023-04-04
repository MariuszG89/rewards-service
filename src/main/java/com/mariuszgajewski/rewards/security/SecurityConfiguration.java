package com.mariuszgajewski.rewards.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/rewards-service/**")
                .authenticated()
                .and()
                .authorizeRequests()
                .antMatchers("/swagger-ui/**").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/v3/api-docs.yaml").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable()
                .httpBasic();
        return http.build();
    }
}
