package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @PersistenceContext
    EntityManager em;

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

        List<Member> memberList = memberRepository.ageFind(20);

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

    @Test
    public void testMultiParameterFind() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("BBB").age(20).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        assertThat(memberList).isEqualTo(Stream.of(member1, member2).toList());
    }

    @Test
    public void testReturnType() {
        Member member1 = Member.builder().username("AAA").age(10).build();
        Member member2 = Member.builder().username("AAA").age(20).build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> memberList = memberRepository.findListByUsername("AAA");
//        Member member = memberRepository.findMemberByUsername("AAA");
//        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");

        //두 개 이상일시 해당 예외 터짐 : jpa 예외가 spring 예외로 변환됨. DB의 다형성을 위해.
        assertThatThrownBy(() -> memberRepository.findMemberByUsername("AAA")).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    public void paging() {
        //given
        memberRepository.save(Member.builder().username("member1").age(10).build());
        memberRepository.save(Member.builder().username("member2").age(10).build());
        memberRepository.save(Member.builder().username("member3").age(10).build());
        memberRepository.save(Member.builder().username("member4").age(10).build());
        memberRepository.save(Member.builder().username("member5").age(10).build());

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> pageByAge = memberRepository.findPageByAge(age, pageRequest);

        Page<MemberDTO> map = pageByAge.map(member -> new MemberDTO(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = pageByAge.getContent();
        long totalCount = pageByAge.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
        assertThat(pageByAge.getNumber()).isEqualTo(0);
        assertThat(pageByAge.getTotalPages()).isEqualTo(2);
        assertThat(pageByAge.isFirst()).isTrue();
    }

    @Test
    public void slicing() {
        //given
        memberRepository.save(Member.builder().username("member1").age(10).build());
        memberRepository.save(Member.builder().username("member2").age(10).build());
        memberRepository.save(Member.builder().username("member3").age(10).build());
        memberRepository.save(Member.builder().username("member4").age(10).build());
        memberRepository.save(Member.builder().username("member5").age(10).build());

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> pageByAge = memberRepository.findSliceByAge(age, pageRequest);

        //then
        List<Member> content = pageByAge.getContent();
//        long totalCount = pageByAge.getTotalElements();

        assertThat(content.size()).isEqualTo(3);
//        assertThat(totalCount).isEqualTo(5);
        assertThat(pageByAge.getNumber()).isEqualTo(0);
//        assertThat(pageByAge.getTotalPages()).isEqualTo(2);
        assertThat(pageByAge.isFirst()).isTrue();
    }

    @Test
    public void bulkUpdate() {
        //given
        memberRepository.save(Member.builder().username("member1").age(10).build());
        memberRepository.save(Member.builder().username("member2").age(11).build());
        memberRepository.save(Member.builder().username("member3").age(12).build());
        memberRepository.save(Member.builder().username("member4").age(13).build());
        memberRepository.save(Member.builder().username("member5").age(14).build());

        //when
        int age = 12;
        long updateCount = memberRepository.bulkAgePlus(age);
        System.out.println("updateCount = " + updateCount);

//        em.clear();

        //then
        List<Member> all = memberRepository.ageFind(13);
        all.forEach(System.out::println);
        assertThat(all).allMatch(member -> member.getAge() >= 13);
        assertThat(updateCount).isEqualTo(3);
    }

    @Test
    public void entityGraph() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(Member.builder().username("member1").age(10).team(teamA).build());
        memberRepository.save(Member.builder().username("member2").age(11).team(teamB).build());

        em.flush();
        em.clear();

        //when
        List<Member> all = memberRepository.findAll();

        //then
        all.forEach(member -> assertThat(member.getTeam()).isInstanceOf(Team.class));
    }

    @Test
    public void queryHint() {
        //given
        Team teamA = Team.builder().name("teamA").build();
        Team teamB = Team.builder().name("teamB").build();
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = Member.builder().username("member1").age(10).team(teamA).build();
        memberRepository.save(member1);

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyById(member1.getId());
        findMember.changeTeam(teamB);

        em.flush();
        em.clear();

        //then
        Member findMember1 = memberRepository.findReadOnlyById(member1.getId());
        assertThat(findMember1.getTeam().getName()).isEqualTo("teamA");
    }

    @Test
    public void jpaLock() {
        //given
        Member member1 = Member.builder().username("member1").age(10).build();
        memberRepository.save(member1);

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findLockById(member1.getId());
    }

    @Test
    public void customRepository() {
        //given
        Member member1 = Member.builder().username("member1").age(10).build();
        memberRepository.save(member1);
        //when
        List<Member> memberCustom = memberRepository.findMemberCustom();

        //then
        assertThat(memberCustom.get(0)).isEqualTo(member1);
    }

    @Test
    public void specBasic() {
        //given
        Team team = Team.builder().name("teamA").build();
        em.persist(team);
        Member member1 = Member.builder().username("member1").age(10).team(team).build();
        Member member2 = Member.builder().username("member2").age(10).team(team).build();
        em.persist(member1);
        em.persist(member2);
        em.flush();
        em.clear();

        //when
        Specification<Member> spec = MemberSpec.userName("member1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void queryByExample() {
        //given
        Team team = Team.builder().name("teamA").build();
        em.persist(team);
        Member member1 = Member.builder().username("member1").age(10).team(team).build();
        Member member2 = Member.builder().username("member2").age(10).team(team).build();
        em.persist(member1);
        em.persist(member2);
        em.flush();
        em.clear();

        //when
        //Probe
        Team teamA = Team.builder().name("teamA").build();
        Member member = Member.builder().username("member1").team(teamA).build();

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");
        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("member1");
    }
    
    @Test
    public void projections() {
        //given
        Team team = Team.builder().name("teamA").build();
        em.persist(team);
        Member member1 = Member.builder().username("member1").age(10).team(team).build();
        Member member2 = Member.builder().username("member2").age(10).team(team).build();
        em.persist(member1);
        em.persist(member2);
        em.flush();
        em.clear();

        //when
        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("member1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly.getUsername());
        }

        List<UsernameOnlyDto> result2 = memberRepository.findProjectionsDtoByUsername("member1", UsernameOnlyDto.class);

        for (UsernameOnlyDto usernameOnlyDto : result2) {
            System.out.println("UsernameOnlyDto = " + usernameOnlyDto.getUsername());
        }

        List<NestedClosedProjections> result3 = memberRepository.findProjectionsDtoByUsername("member1", NestedClosedProjections.class);

        for (NestedClosedProjections m : result3) {
            System.out.println("m.getUsername() = " + m.getUsername());
            System.out.println("m.getTeam().getName() = " + m.getTeam().getName());
        }
    }

    @Test
    public void nativeQuery() {
        //given
        Team team = Team.builder().name("teamA").build();
        em.persist(team);
        Member member1 = Member.builder().username("member1").age(10).team(team).build();
        Member member2 = Member.builder().username("member2").age(10).team(team).build();
        em.persist(member1);
        em.persist(member2);
        em.flush();
        em.clear();

        //when
        Member result = memberRepository.findByNativeQuery("member1");
        System.out.println("result = " + result.getUsername());

        //when
        Page<MemberProjection> result1 = memberRepository.findByNativeProjection(PageRequest.of(10, 10));
        List<MemberProjection> content = result1.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }
    }
}