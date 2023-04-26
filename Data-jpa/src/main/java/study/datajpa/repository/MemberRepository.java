package study.datajpa.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.MemberDTO;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3SomethingBy();

//    @Query(name = "Member.ageFind") //Spring Data JPA가 생략해도 작동하게끔 도와준다.
    List<Member> ageFind(@Param("age") int age);

    @Query("select m from Member m where m.username = :username")
    List<Member> usernameFind(@Param("username") String username);

    @Query("select m.username from Member m")
    List<String> usernameList();

    @Query("select new study.datajpa.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTO();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    Page<Member> findPageByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);
    List<Member> findByAge(int age, Sort sort);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();
    @Override
    @EntityGraph(attributePaths = "team")
    List<Member> findAll();
    @Query("select m from Member m")
    @EntityGraph(attributePaths = "team")
    List<Member> findMemberEntityGraph();
    @EntityGraph(attributePaths = "team")
    List<Member> findEntityGraphByUsername(String username);
    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyById(Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockById(Long id);
}
