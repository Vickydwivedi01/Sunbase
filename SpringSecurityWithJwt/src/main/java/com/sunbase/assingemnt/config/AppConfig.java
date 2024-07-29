package com.sunbase.assingemnt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunbase.assingemnt.repository.CustomerRepository;
import com.sunbase.assingemnt.service.CustomerUserDetailsService;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SecurityFilterChain springSecurityConfiguration(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers(HttpMethod.POST, "/customers").permitAll()
            .requestMatchers(HttpMethod.POST, "/signIn").permitAll()
            .requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/fetch-customers").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
            .formLogin().and()
            .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService(CustomerRepository customerRepository) {
//        return new CustomerUserDetailsService(customerRepository);
//    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                   .build();
//    }
}
