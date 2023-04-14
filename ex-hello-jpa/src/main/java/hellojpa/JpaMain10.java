package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain10 {
    public static void main(String[] args) {
        // 10장 객체지향 쿼리 언어1 - 기본 문법
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션의 단위가 일어날 때마다 em을 반드시 만들어 사용하고 버려야한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션을 시작하고 데이터 처리를 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //sample 저장
            Member member = new Member();
            member.setUsername("lee");
            member.setHomeAddress(new Address("city", "street", "zipcode"));
            em.persist(member);

            em.flush();
            em.clear();

            em.createNativeQuery("select MEMBER_ID, city, street, USERNAME from MEMBER").getResultList();

            em.flush();
            em.clear();

            //페이징
            for (int i = 0; i < 100; i++) {
                member = new Member();
                member.setUsername(i + "lee");
                member.setHomeAddress(new Address(i + "city", "street", "zipcode"));
                em.persist(member);
            }
            em.flush();
            em.clear();

            List<Member> result = em.createQuery("select m from Member m order by m.username desc", Member.class)
                    .setFirstResult(2)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size() = " + result.size());

            for (Member m : result) {
                System.out.println("m = " + m);
            }

            // 트랜잭션 커밋.
            tx.commit();
        } catch (Exception e) {
            // 문제 발생시 트랜잭션 롤백.
            tx.rollback();
            e.printStackTrace();
        } finally {
            // 작업이 끝나면 닫아줌.
            em.close();
        }
        emf.close();
    }
}
