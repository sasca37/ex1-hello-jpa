package jpa_basic.ex1hellojpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @OneToOne(mappedBy = "locker") //읽기 전용이됨
    private Member member;
}
