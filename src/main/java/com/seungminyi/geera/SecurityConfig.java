package com.seungminyi.geera;

import com.seungminyi.geera.member.auth.JwtTokenFilter;
import com.seungminyi.geera.member.auth.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    public SecurityConfig(JwtTokenProvider jwtTokenProvider, AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable()
                )
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/members/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.disable()
                );

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain webSecurityFilterChain(HttpSecurity http, AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {
//        http
//                .csrf(csrf -> csrf
//                        .disable()
//                )
//                .exceptionHandling(exceptionHandling -> exceptionHandling
//                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
//                )
//                .authorizeRequests(authorize -> authorize
//                        .anyRequest().authenticated()
//                )
//                .formLogin(formLogin -> formLogin
//                        .successHandler(authenticationSuccessHandler)
//                );
//
//        return http.build();
//    }

}