package com.seungminyi.geera.member;

import org.apache.ibatis.annotations.Mapper;

import com.seungminyi.geera.member.dto.Member;

@Mapper
public interface MemberRepository {
	void insert(Member member);

	Member findByEmail(String email);
}
