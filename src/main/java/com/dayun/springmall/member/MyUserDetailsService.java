package com.dayun.springmall.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 username을 가진 유저를 찾아와서 -> Repository에 직접 함수를 만들어서 사용
        Optional<Member> result = memberRepository.findByUsername(username);
        // Optional 타입은 if를 써야 안전하다
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("아이디를 찾을 수 없습니다");
        }
        Member user = result.get();

        // user가 사이트에서 갖는 권한을 List 자료형으로 만들기 -> API에서 현재 유저의 권한을 출력해볼 수 있다.
        // user마다 다른 권한을 주고 싶다면 if문을 사용하자
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("일반유저"));

        // User(username, password, 권한) 해주세요
        CustomUser customUser = new CustomUser(user.getUsername(), user.getPassword(), authorities);
        customUser.displayName = user.getDisplayName();

        return customUser;
    }
}

// 유저정보 커스터마이징 -> Java 상속 문법 사용
class CustomUser extends User {

    public String displayName;

    public CustomUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        // extends로 복사한 class의 constructor
        super(username, password, authorities);
    }
}