package com.seungminyi.geera.auth;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {

	private JwtTokenProvider jwtTokenProvider;

	public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = jwtTokenProvider.resolveToken(request);

		if (token != null) {
			if (jwtTokenProvider.validateToken(token)) {
				SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write("유효하지 않은 토큰입니다.");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}
