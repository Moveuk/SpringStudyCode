package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId(); // 커맨드와 쿼리를 분리해라. 부작용 방지를 위해 리턴값을 되도록 줄인다.
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
