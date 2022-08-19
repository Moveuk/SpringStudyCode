package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션의 단위가 일어날 때마다 em을 반드시 만들어 사용하고 버려야한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션을 시작하고 데이터 처리를 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
//            //회원 작성
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");
//            // 데이터베이스에 밀어 넣는다.
//            em.persist(member);

            //회원 조회
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember = " + findMember.getId());
//            System.out.println("findMember = " + findMember.getName());

            //회원 삭제
            // 조회한 객체를 remove 메소드를 사용해 삭제함.
//            em.remove(findMember);

            //회원 수정
            // 조회한 객체를 변경하면 커밋시 영속성 컨텍스트에서 변환된 것을 flush 하여 내보냄
            // 즉, 변환된 값을 기억하고 있다가 수정 쿼리가 나감.
//            findMember.setName("helloJPA");

            //JPQL
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .setFirstResult(5)  //5번부터
//                    .setMaxResults(8)   //8개 가져오기
//                    .getResultList();   //쿼리로 리스트 받아옴.

            //MySQL - Limit ? Offset ?
            //Oracle - rownum

            //영속성 컨텍스트 설명
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA");
            //지금까지는 비영속 상태

            //EntityManager를 통해서 영속 상태로 변경
//            System.out.println("=== Before ===");
//            em.persist(member);
//            System.out.println("=== After ===");

//            Member findMember = em.find(Member.class, 101L);

//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());

            //조회 테스트
//            Member findMember1 = em.find(Member.class, 101L);
//            Member findMember2 = em.find(Member.class, 101L);
//
//            System.out.println(findMember1 == findMember2); //true

            //쓰기지연 실습코드
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(151L, "B");
//
//            em.persist(member1);
//            em.persist(member2);
//
//            System.out.println("-----------------");

            //더티체킹 실습코드
//            Member member = em.find(Member.class, 150L);
//            member.setName("ZZZZZ");

            //컬렉션 다루듯 사용하기 때문에 따로 persist를 할 필요가 없다
//            em.persist(member); //X
//
//            //삭제
//            em.remove(member);

            //플러시 실습코드
//            Member member = new Member(200L, "200");
//            em.persist(member);

            //flush: 트랜잭션이 끝나기 전 미리 보내고 싶을 때
//            em.flush();
//            System.out.println("-----------------");

            //준영속 상태 실습
//            Member member = em.find(Member.class, 150L);
//            member.setName("AAAAA");

            //준영속 상태로 변경. ZZZZZ가 AAAAA로 변경되지 않음 member를 컨텍스트가 관리하지 않기 때문.
//            em.detach(member);
            //영속성 컨텍스트 아예 삭제
//            em.clear();
            //영속성 컨텍스트 닫음
//            em.close();

            // 트랜잭션 커밋.
            tx.commit();
        } catch (Exception e) {
            // 문제 발생시 트랜잭션 롤백.
            tx.rollback();
        } finally {
            // 작업이 끝나면 닫아줌.
            em.close();
        }
        emf.close();
    }
}
