package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class JpaMain8 {
    public static void main(String[] args) {
        // 6장 다양한 연관관계 매핑
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션의 단위가 일어날 때마다 em을 반드시 만들어 사용하고 버려야한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션을 시작하고 데이터 처리를 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //sample 저장
            Member member = new Member();
            member.setUsername("1st member");

            em.persist(member);

            em.flush();
            em.clear();

//            Member refMember = em.find(Member.class, member.getId());
            Member refMember = em.getReference(Member.class, member.getId());
            System.out.println("refMember.getId() = " + refMember.getId());
//            System.out.println("refMember.getUsername() = " + refMember.getUsername());
//
//            System.out.println("refMember.getClass() = " + refMember.getClass().getName());

//            em.detach(refMember);

            System.out.println("refMember.getUsername() = " + refMember.getUsername());

            //프록시 초기화 여부 확인
            System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(refMember));

            //하이버네이트의 강제 초기화
            Hibernate.initialize(refMember);

            em.flush();
            em.clear();

            Team teamA = new Team();
            teamA.setName("teamA");

            Member member1 = em.find(Member.class, member.getId());
            member1.setTeam(teamA);

            em.persist(member1);
            em.persist(teamA);

            em.flush();
            em.clear();

            Member memberForLazy = em.find(Member.class, member.getId());
            System.out.println("memberForLazy.getTeam().getClass() = " + memberForLazy.getTeam().getClass());

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            em.flush();
            em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildren().remove(0);



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
