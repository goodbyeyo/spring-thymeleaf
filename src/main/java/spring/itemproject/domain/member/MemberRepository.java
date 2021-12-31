package spring.itemproject.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();   // static 사용
    private static long sequence = 0L;  // static  사용

    public Member save(Member member) {
        member.setId((++sequence));
        log.info("save : member= {}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId){
        return findAll().stream()
        .filter(m -> m.getLoginId().equals(loginId))
        .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    /*
    public Member findByLoginId2(String loginId) {
        List<Member> all = findAll();
        for (Member membmer : all) {
            if (membmer.getLoginId().equals(loginId)) {
                return membmer;

            }
        }
        return null;
    }
    */

    /*
    public Optional<Member> findByLoginId3(String loginId) {
        List<Member> all = findAll();
        for (Member membmer : all) {
            if (membmer.getLoginId().equals(loginId)) {
                return Optional.of(membmer);
            }
        }
        return Optional.empty();
    }
    */

}
