package spring.itemproject.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.itemproject.domain.member.Member;
import spring.itemproject.domain.member.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     *
     * @param loginId
     * @param password
     * @return null 이면 로그인실패
     */

    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

        /*
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
        byLoginId.filter(m -> m.getPassword().equals(password))
                .orElse(null);
        */

        /*
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get();
        if (member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }
        */
    }
}
