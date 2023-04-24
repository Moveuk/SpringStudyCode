package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member memberA = Member.builder().username("memberA").build();
        Member savedMember = memberRepository.save(memberA);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    public void basicCRUD() {
        long standardCount = memberRepository.count();

        Member member1 = Member.builder().username("member1").build();
        Member member2 = Member.builder().username("member2").build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(findMember1);
        assertThat(findMember2).isEqualTo(findMember2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2 + standardCount);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2 + standardCount);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(standardCount);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(memberList.get(0).getUsername()).isEqualTo("AAA");
        assertThat(memberList.get(0).getAge()).isEqualTo(20);
        assertThat(memberList.size()).isEqualTo(1);
    }

    @Test
    public void findTop3SomethingBy() {
        List<Member> memberList = memberRepository.findTop3SomethingBy();
    }

    @Test
    public void testNamedQuery() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.ageFind( 20);

        assertThat(memberList.get(0).getAge()).isEqualTo(20);
        assertThat(memberList.size()).isEqualTo(1);
    }

    @Test
    public void testQuery() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.usernameFind("AAA");

        assertThat(memberList.get(0).getUsername()).isEqualTo("AAA");
        assertThat(memberList.size()).isEqualTo(2);
    }

    @Test
    public void testQueryValue() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> memberList = memberRepository.usernameList();

        assertThat(memberList).allMatch(x -> x.equals("AAA"));
    }

    @Test
    public void testQueryDTO() {
        Team teamA = Team.builder().name("TeamA").build();
        teamRepository.save(teamA);
        Member member1 = Member.builder().username("AAA").age(10).team(teamA).build();
        Member member2 = Member.builder().username("AAA").age(20).team(teamA).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDTO> memberDTOList = memberRepository.findMemberDTO();

        assertThat(memberDTOList).allMatch(x -> x.getTeamName().equals("TeamA"));
    }
}