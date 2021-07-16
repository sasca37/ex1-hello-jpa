package jpa_basic.ex1hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        //트랜잭션 시작, commit을 설정해야지만 등록이 된다.
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        //실제 동작 코드
        try {
            Member member = new Member();
            member.setUsername("AbA");

            em.persist(member);
            System.out.println("member.getId() = " + member.getId());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
