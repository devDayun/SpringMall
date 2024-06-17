package com.dayun.springmall.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    String memberJoinForm() {

        return "memberRegister";
    }

    // 회원가입 폼의 action, method와 같이 작성
    @PostMapping("/register")
    String joinMember(@ModelAttribute Member member) {
        // 비밀번호는 해싱해서 저장하기
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
        log.info("member={}", member);
        return "redirect:/list";
    }

    // 로그인 페이지로 이동, 실제 로그인은 Spring Security가 처리
    @GetMapping("/login")
    String login() {
        return "login";
    }

    @GetMapping("/my-page")
    String myPage(Authentication auth) {
        // 형 변환
        // getPrincipal()함수에서 캐스팅을 권장함
        CustomUser result = (CustomUser)auth.getPrincipal();

        log.info("displayName={}", result.displayName);
        return "myPage";
    }

}