package com.raulino.jwtstudyspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.raulino.jwtstudyspring.services.LocalUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new LocalUserDetailsService();
    }
}
