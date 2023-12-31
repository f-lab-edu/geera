package com.seungminyi.geera.auth.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.seungminyi.geera.member.dto.Member;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CustomUserDetails implements UserDetails {
	private Long id;
	private String email;
	private String name;
	private String password;

	public CustomUserDetails(Member member) {
		this.id = member.getMemberId();
		this.email = member.getEmail();
		this.name = member.getName();
		this.password = member.getPassword();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
