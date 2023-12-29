package com.seungminyi.geera.member;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seungminyi.geera.member.dto.Member;
import com.seungminyi.geera.member.dto.ProjectInfo;
import com.seungminyi.geera.project.ProjectMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ProjectMemberRepository projectMemberRepository;
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

	//TODO 본인이 초대받은 프로젝트만 조회가능
    public List<ProjectInfo> getInvitedProjects(Long memberId) {
	    return projectMemberRepository.getInvitedProjects(memberId);
    }
}
