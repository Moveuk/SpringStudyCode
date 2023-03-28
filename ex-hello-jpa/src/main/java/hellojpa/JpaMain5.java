package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain5 {
    public static void main(String[] args) {
        // 5장 연관관계의 매핑
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 트랜잭션의 단위가 일어날 때마다 em을 반드시 만들어 사용하고 버려야한다.
        EntityManager em = emf.createEntityManager();

        // 트랜잭션을 시작하고 데이터 처리를 한다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
//            member.setTeamId(team.getId());
            member.setTeam(team);
            em.persist(member);

            em.flush(); //영속성 컨텍스트 더티체킹 SQL 내보냄
            em.clear(); //영속성 컨텍스트 클리어 -> 모든 객체가 삭제되어 다음부터는 새롭게 select해옴

            // 조회
            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }

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
