package spring.itemproject.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spring.itemproject.domain.login.LoginService;
import spring.itemproject.domain.member.Member;
<<<<<<< HEAD
import spring.itemproject.web.SessionConst;
import spring.itemproject.web.session.SessionManager;
=======
>>>>>>> parent of 3525fe8 (로그인 세션 직접 만들어서 적용하기)

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        // 로그인 성공 처리 TODO

        // 웹 브라우저는 종료전까지 회원의 id를 서버에 계속 보내줌
        Cookie isCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(isCookie);

        return "redirect:/";
    }

<<<<<<< HEAD
    // @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        // 로그인 성공 처리 TODO
        // 세션 관리자를 통해 세션을 생성하고, 회원 데이터 보관
        sessionManager.createSession(loginMember, response);
        // 웹 브라우저는 종료전까지 회원의 id를 서버에 계속 보내줌
        // Cookie isCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        // response.addCookie(isCookie);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성, request.getSession 과 request.getSession(true) 동일
        HttpSession session = request.getSession(true);

        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }

    // @PostMapping("/logout")
=======
    @PostMapping("/logout")
>>>>>>> parent of 3525fe8 (로그인 세션 직접 만들어서 적용하기)
    public String logout(HttpServletResponse response, String cookieName) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

<<<<<<< HEAD
    // @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request, String cookieName) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request, String cookieName) {
        // 신규 세션을 생성하지 않는다
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 삭제
        }
        return "redirect:/";
    }

=======
>>>>>>> parent of 3525fe8 (로그인 세션 직접 만들어서 적용하기)
    private void expireCookie(HttpServletResponse response, String memberId) {
        Cookie cookie = new Cookie(memberId, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
