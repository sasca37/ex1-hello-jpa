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
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setTeam(team);
            em.persist(member);
 //           team.getMembers().add(member);
            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId()); // first cache
            List<Member> members = findTeam.getMembers();
            System.out.println("=================================");
            for (Member m : members) {
                System.out.println("@@@@@@@@@@@@m = " + m.getUsername());
            }
            System.out.println("=================================");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
