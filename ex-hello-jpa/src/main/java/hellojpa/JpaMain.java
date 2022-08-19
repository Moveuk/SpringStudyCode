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
            Member findMember = em.find(Member.class, 1L);
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
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(5)  //5번부터
                    .setMaxResults(8)   //8개 가져오기
                    .getResultList();   //쿼리로 리스트 받아옴.

            //MySQL - Limit ? Offset ?
            //Oracle - rownum

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
