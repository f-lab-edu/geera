package com.seungminyi.geera.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberRepository {
	void insert(Member member);

	Member findMemberByEmail(String email);
}
