package com.ProyectoDeAula5.Proyecto5.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/Administrador/**").hasRole("Administrador")
                        .requestMatchers("/Empleado/**").hasRole("Empleado")
                        .anyRequest().permitAll() // lo demás se permite
                )
                .httpBasic(); // o .formLogin() si usas formularios

        return http.build();
    }
}
