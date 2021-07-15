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
//            Member findMember = em.find(Member.class, 1L);
            List<Member> result = em.createQuery("select m from Member as m ", Member.class)
                    .setFirstResult(1).setMaxResults(10).getResultList();

            for (Member member : result) {
                System.out.println("member = " + member.getName());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
