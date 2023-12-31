package com.seungminyi.geera.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.seungminyi.geera.auth.dto.CustomUserDetails;
import com.seungminyi.geera.member.MemberRepository;
import com.seungminyi.geera.member.dto.Member;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

    public CustomUserDetailsService(MemberRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = userRepository.findByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("아이디를 찾을수 없습니다 : " + email);
        }
        return new CustomUserDetails(member);
    }
}
