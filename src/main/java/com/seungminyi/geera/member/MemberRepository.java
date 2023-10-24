package com.seungminyi.geera.member;

import org.apache.ibatis.annotations.Mapper;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

@Mapper
public interface MemberRepository {
    void insert(Member member);

    Member findMemberByEmail(String id);
}
