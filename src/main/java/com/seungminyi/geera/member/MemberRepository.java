package com.seungminyi.geera.member;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberRepository {
    void insertMember(Member member);

    Member findMemberById(String email);
}
