package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain6 {
    public static void main(String[] args) {
        // 6장 다양한 연관관계 매핑
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션의 단위가 일어날 때마다 em을 반드시 만들어 사용하고 버려야한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션을 시작하고 데이터 처리를 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //저장
            Member member = new Member();
            member.setUsername("member1");

            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            //member update 쿼리가 나감. 일대다가 주인인 경
            team.getMembers().add(member);

            em.persist(team);

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
