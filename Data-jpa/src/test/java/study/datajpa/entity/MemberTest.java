package study.datajpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = Member.builder().username("member1").age(10).team(teamA).build();
        Member member2 = Member.builder().username("member2").age(20).team(teamB).build();
        Member member3 = Member.builder().username("member3").age(30).team(teamA).build();
        Member member4 = Member.builder().username("member4").age(40).team(teamB).build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("team = " + member.getTeam());
        }
    }

    @Test
    public void testEventBaseEntity() {
        //given
        Team teamA = Team.builder().name("TeamA").build();
        Team teamB = Team.builder().name("TeamB").build();
        em.persist(teamA); //@PrePersist
        em.persist(teamB); //@PrePersist

        Member member1 = Member.builder().username("member1").age(10).build();
        em.persist(member1); //@PrePersist

        member1.changeTeam(teamB);

        em.flush(); //@PreUpdate
        em.clear();

        //when
        Member findMember = em.createQuery("select m from Member m where m.id = :id", Member.class)
                .setParameter("id", member1.getId())
                .getSingleResult();


        //then
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdatedDate() = " + findMember.getUpdatedDate());
    }
}