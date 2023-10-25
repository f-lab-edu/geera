package com.seungminyi.geera.config;

import com.seungminyi.geera.member.auth.JwtTokenFilter;
import com.seungminyi.geera.member.auth.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
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
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().requestMatchers(
                "/api/login",
                "/members/**"
        );
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
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .disable()
                );

        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .csrf(csrf -> csrf.disable())
//                .exceptionHandling(exch -> exch
//                        .authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
//                .authorizeHttpRequests(authz -> authz
//                        .anyRequest().authenticated())
//                .formLogin(form -> form
//                        .successHandler(authenticationSuccessHandler))
//                .logout(logout -> logout
//                        .logoutUrl("/logout"))
//                .build();
//    }

}