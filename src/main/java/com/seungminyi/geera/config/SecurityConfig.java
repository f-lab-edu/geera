package com.seungminyi.geera.config;

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
import org.springframework.web.cors.CorsUtils;

import com.seungminyi.geera.auth.JwtTokenFilter;
import com.seungminyi.geera.auth.JwtTokenProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationSuccessHandler authenticationSuccessHandler;

	public SecurityConfig(JwtTokenProvider jwtTokenProvider,
		AuthenticationSuccessHandler authenticationSuccessHandler) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.authenticationSuccessHandler = authenticationSuccessHandler;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
		return (web) -> web.ignoring().requestMatchers(
			"/api/login",
			"/members/**",
			"/docs/**",
			"/v3/**",
			"/error/**"
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
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.requestMatchers("/error**").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.disable()
			);

		return http.build();
	}
}

