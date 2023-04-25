package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.MemberDTO;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    public List<Member> findTop3SomethingBy();

//    @Query(name = "Member.ageFind") //Spring Data JPA가 생략해도 작동하게끔 도와준다.
    public List<Member> ageFind(@Param("age") int age);

    @Query("select m from Member m where m.username = :username")
    public List<Member> usernameFind(@Param("username") String username);

    @Query("select m.username from Member m")
    public List<String> usernameList();

    @Query("select new study.datajpa.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    public List<MemberDTO> findMemberDTO();

    @Query("select m from Member m where m.username in :names")
    public List<Member> findByNames(@Param("names") Collection<String> names);

    public List<Member> findListByUsername(String username);
    public Member findMemberByUsername(String username);
    public Optional<Member> findOptionalByUsername(String username);

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    public Page<Member> findPageByAge(int age, Pageable pageable);
    public Slice<Member> findSliceByAge(int age, Pageable pageable);
    public List<Member> findByAge(int age, Sort sort);
}
