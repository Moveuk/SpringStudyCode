package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.MemberDTO;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    public List<Member> findTop3SomethingBy();

//    @Query(name = "Member.ageFind") //Spring Data JPA가 생략해도 작동하게끔 도와준다.
    public List<Member> ageFind(@Param("age") int age);

    @Query("select m from Member m where m.username >= :username")
    public List<Member> usernameFind(@Param("username") String username);

    @Query("select m.username from Member m")
    public List<String> usernameList();

    @Query("select new study.datajpa.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    public List<MemberDTO> findMemberDTO();
}
