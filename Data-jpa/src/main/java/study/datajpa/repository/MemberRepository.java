package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    public List<Member> findTop3SomethingBy();

//    @Query(name = "Member.ageFind") //Spring Data JPA가 생략해도 작동하게끔 도와준다.
    public List<Member> ageFind(@Param("age") int age);
}
