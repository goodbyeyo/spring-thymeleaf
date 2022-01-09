package spring.itemproject.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import spring.itemproject.domain.member.Member;
import spring.itemproject.domain.member.MemberRepository;
import spring.itemproject.web.argumentresolver.Login;
import spring.itemproject.web.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 로그인 처리까지 되는 홈화면
    // @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }

        // 로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    // @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        
        
        HttpSession session = request.getSession(false);
        // 세션이 null 이면 홈화면으로 이동 로그인할수있게
        if (session == null) {
            return "home";
        }

        // type casing
        Member member = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        
        // 세션에 회원데이터가 없으면 home
        if (member == null) {
            return "home";
        }
        
        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }
    
    // @SessionAttribute 는 세션을 생성하지 않는다. 즉 로그인된 상요자를 찾을때 사용하는 어노테이션
    // @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {
        
        // 세션에 회원데이터가 없으면 home
        if (member == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")    // ModelAttribute 처럼 동작하지 않도록 만들어줘야한다...
    public String homeLoginV3ArgumentResolver(@Login Member member, Model model) {
        // @Login 어노테이션이 있으면 직접 만든 `ArgumentResolver`가 동작해서
        // 자동으로 세션에 있는 로그인 회원을 찾아주고 만약 세션에 없다면 null 을 반환
        // 세션에 회원데이터가 없으면 home
        if (member == null) {
            return "home";
        }

        // 세션이 유지되면 로그인으로 이동
        model.addAttribute("member", member);
        return "loginHome";
    }
}
