package com.seungminyi.geera.member;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seungminyi.geera.member.dto.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void registerMember(Member member) throws DataIntegrityViolationException {
		// TODO: accept encoded password in the first place
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		member.setPassword(encodedPassword);
		memberRepository.insert(member);
	}

	public Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email);
	}
}
