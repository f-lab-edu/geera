package com.seungminyi.geera.member.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seungminyi.geera.member.Member;
import com.seungminyi.geera.member.MemberRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository userRepository;

	public CustomUserDetailsService(MemberRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = userRepository.findMemberByEmail(email);
		if (member == null) {
			throw new UsernameNotFoundException("아이디를 찾을수 없습니다 : " + email);
		}
		return new CustomUserDetails(member);
	}
}
