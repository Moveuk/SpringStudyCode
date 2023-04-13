package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain9 {
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
            Member member1 = new Member();
            member1.setUsername("1st member");
            Address workAddress = new Address("city", "street", "zipcode");
            member1.setWorkAddress(workAddress);

            em.persist(member1);

            Address copyAddress = new Address(workAddress.getCity(), workAddress.getStreet(), workAddress.getZipcode());

            Member member2 = new Member();
            member2.setUsername("2st member");
            member2.setWorkAddress(copyAddress);

            em.persist(member2);

            Address newAddress = new Address("NewCity", workAddress.getStreet(), workAddress.getZipcode());
            member1.setWorkAddress(newAddress);

            em.persist(member1);

            //값 타입의 컬렉션
            member1.setHomeAddress(new Address("city", "street", "zipcode"));

            member1.getFavoriteFoods().add("치킨");
            member1.getFavoriteFoods().add("피자");
            member1.getFavoriteFoods().add("족발");

            member1.getAddressHistory().add(new AddressEntity("old1", "street", "10000"));
            member1.getAddressHistory().add(new AddressEntity("old2", "street", "10000"));

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            //집주소 변경하고 싶은 경우 통째로 값타입 인스턴스를 변경해야함.
            Address homeAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("NewCity", homeAddress.getStreet(), homeAddress.getZipcode()));

            //치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            //주소 컬렉션 변경
            findMember.getAddressHistory().remove(new AddressEntity("old1", "street", "10000"));
            findMember.getAddressHistory().add(new AddressEntity("new1", "street", "10000"));

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
