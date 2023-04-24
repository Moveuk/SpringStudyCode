package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;
    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Test
    public void testMember() {
        Member memberA = Member.builder().username("memberA").build();
        Member savedMember = memberJpaRepository.save(memberA);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    public void basicCRUD() {
        long standardCount = memberJpaRepository.count();

        Team team = Team.builder().build();
        teamJpaRepository.save(team);
        Member member1 = Member.builder().username("member1").team(team).build();
        Member member2 = Member.builder().username("member2").team(team).build();
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(findMember1);
        assertThat(findMember2).isEqualTo(findMember2);

        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2 + standardCount);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2 + standardCount);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(standardCount);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> memberList = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(memberList.get(0).getUsername()).isEqualTo("AAA");
        assertThat(memberList.get(0).getAge()).isEqualTo(20);
        assertThat(memberList.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> memberList = memberJpaRepository.ageFind( 20);

        assertThat(memberList.get(0).getAge()).isEqualTo(20);
        assertThat(memberList.size()).isEqualTo(1);
    }

    @Test
    public void paging() {
        //given
        memberJpaRepository.save(Member.builder().username("member1").age(10).build());
        memberJpaRepository.save(Member.builder().username("member2").age(10).build());
        memberJpaRepository.save(Member.builder().username("member3").age(10).build());
        memberJpaRepository.save(Member.builder().username("member4").age(10).build());
        memberJpaRepository.save(Member.builder().username("member5").age(10).build());

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> byPage = memberJpaRepository.findByPage(age, offset, limit);
        long totalcount = memberJpaRepository.totalcount(age);

        assertThat(byPage.size()).isEqualTo(3);
        assertThat(totalcount).isEqualTo(5);
    }
}