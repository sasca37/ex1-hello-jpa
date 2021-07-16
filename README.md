
JPA
- 처음 객체 생성 : 비영속 상태 
- .persist() :영속 상태
영속성 컨텍스트 

commit 시에 시작 
1. flush()

2. 엔티티와 스냅샷 비교 (변경감지)
<구조>
-[1차 캐시 ] : @Id , Entity : 한번 쿼리날렸던 내용은 1차캐시에 남아서 db접근없이 가져올 수있다.
@ID,  Entity,  스냅샷 (최소 Entity)
  
3. UPDATE, DELETE 등 SQL 생성 (쓰기 지연 SQL 저장소)

4. DB로 flush발생 ( 영속성 컨텍스트의 변경내용을 DB에 반영) 
em.flush() - 직접호출 , 트랜잭션 커밋 - 자동 호출 , JPQL 쿼리 실행 - 자동호출 
- 변경 감지
- 지연 SQL 저장소에 등록 (등록, 수정, 삭제 )
- 지연 SQL 저장소의 쿼리를 디비로 전송  

ㅁ 플러시 모드 
- FlushModeType.AUTO : 커밋이나 쿼리를 실행할 때 플러시 (기본값)
- FlushModeType.COMMIT : 커밋할 때만 플러시 

5. DB로 commit

<준영속 상태>  : 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(Detached)
-> 영속성 컨텍스트가 제공하는 기능을 사용 못함
em.detach() 특정 , em.clear() P.C 전체 초기화, em.close() P.C 종료


JPA 중요한 2가지
1. 객체와 관계형 DB 매핑하기 (Object Relational Mapping)
2. 영속성 컨텍스트
- 장점
1. 1차 캐시 
2. 동일성 보장 (a==b true) 
3. 트랜잭션을 지원하는 쓰기 지연
4. 변경 감지
5. 지연 로딩 : 지연을 통해 쿼리를 나중에 날리는 것 


------------------엔티티 매핑----------------
ㅁ 객체와 테이블 매핑 : @Entity, @Table
ㅁ 필드와 컬럼 매핑  : @Column
ㅁ 기본키 매핑 : @Id
ㅁ 연관관계 매핑 : @ManyToOne, @JoinColumn 등 

@Entity : 붙는 순간 JPA가 관리 
* 기본 생성자 필수 (파라미터가 없는 public, protected  생성자 )
* final 클래스 , enum , interface, inner 클래스 사용 x
* 저장할 필드에 final 사용 x 

- ------DB 스키마 자동 생성 ----------------
* DDL을 어플리케이션 실행 시점에 자동 생성 
* 테이블 중심 -> 객체 중심
* DB 방언을 활용해서 DB에 맞는 적절한 DDL 생성 
* 이렇게 생성된 DDL은 개발 장비에서만 (운영 서버에서 사용 시 다듬어야 한다) 
- create (있을 시 drop + create)
- create-drop (위와 동일하고 + 종료시 drop)
- update : 변경분만 반영 (운영 db에는 사용x , 지우는 건 안된다.)
- validate : 엔티티와 테이블이 정상 매핑되었는지 확인 
- none : 아무것도 없을때랑 동일 

※ 자동생성 시 - 주의점
- 운영장비에는 절대 create , create-drop, update 사용 하면 안된다.
- 개발 초기 단계는 create, update 사용
- 테스트 : update, validate 
- 스테이징 및 운영 서버 : validate, none   

----------------필드 매핑 ----------------
@Column(name = "name")
-> insertable, updatable (default = true) 
-> nullable() , unique() : 유니크는 잘 안씀 (제약조건이 이상한 값으로 나와서 @Table에다 씀) 
-> length, columnDefinition (컬럼 정보 직접 삽입 ex( varchar(100) default '/') )
-> BigDecimal 일경우 (precision : 소수점 전체 자릿수, scale : 소수의 자릿수) 
 
@Enumerated(EnumType.STRING)
-> default : ORDINAL (이넘의 순서를 디비에 저장) -> 숫자로 저장되기때문에 사용X
      STRING : 값을 디비에 저장 
@Temporal(TemporalType.TIMESTAMP) Date 날짜 , Time 시간 , Timestamp 둘다 
@Lob : 큰용량  (따로 설정 없으며 문자면 clob, 바이트 등 나머지는 blob)
@Transient : DB랑 관련이 없도록 설정

----------------- 기본키 매핑 -----------------
직접 할당 : @Id 
자동 생성 : @GeneratedValue 
 * IDENTITY : DB에 위임 , MYSQL 
->JPA에서 값을 설정안하므로 DB에 값이 들어가봐야 PK값을 알 수 있기 때문에 
persist() : 영속상태가 되자마자 insert를 실행한다. 그렇게 pk값을 가져와서 jpa에 pk로등록 

 * SEQUENCE : DB 시퀸스 오브젝트 사용, ORACLE (@SequenceGenerator 필요)
-> allocationSize 로 넥스트 밸류를 지정하여 디비에 개수만큼 미리 올려놓는다.  
(동시성 이슈 없이 다양한 문제 해결 가능, 50~100 이 적당)

 * TABLE : 키 생성용 테이블 사용, 모든 DB에서 사용 (@TableGenerator 필요)
-> 장점 : 모든 DB에 적용, 단점 : 성능 , 잘 안쓴다 

 * AUTO : 방언에 따라 자동 지정 

----------권장하는 식별자 전략 --------------------

• 기본 키 제약 조건: null 아님, 유일, 변하면 안된다.
• 미래까지 이 조건을 만족하는 자연키는 찾기 어렵다. 대리키(대
체키)를 사용하자.
• 예를 들어 주민등록번호도 기본 키로 적절하기 않다.
• 권장: Long형(10억 이상데이터) + 대체키 + 키 생성전략 사용


---------------- 요구사항 분석 ----------------
• 회원은 상품을 주문할 수 있다.
• 주문 시 여러 종류의 상품을 선택할 수 있다.

• 회원 기능
  • 회원등록
  • 회원조회
• 상품 기능
  • 상품등록
  • 상품수정
  • 상품조회
• 주문 기능
  • 상품주문
  • 주문내역조회
  • 주문취소


------- 객체와 테이블 연관관계의 차이 ---------
○ 객체의 참조(getter)와 테이블의 외래 키를 매핑
★ 연관 관계의 주인 ( 어려움) 

객체를 테이블에 맞추어 데이터 중심으로 모델링하면 협력관계를 만들 수없다.
- 테이블은 외래키로 조인, 객체는 참조해서 객체를 찾는 패러다임의 차이 때문이다. 

<객체 기준으로 매핑 , 연관관계설정>
// @Column(name = "TEAM_ID")
//    private Long teamId;
    @ManyToOne
    @JoinColumn(name = "TEAM_ID") //TEAM_ID JOIN 
    private Team team;

---------- 양방향 연관관계와 연관관계의 주인 ----------
객체 연관관계를 양방향으로 수정 (추가된 방향으로 가기위한 참조설정필요)
-> 테이블 연관관계는 그대로. 왜냐 외래키로 조인하면 양방향 검색이 가능하니까 

 @OneToMany(mappedBy = "team") ★★★team은 Member의 변수명 지정 (연결을 위한)
    private List<Member> members = new ArrayList<>(); *add시 널방지를위해 초기화가 관례 

- 객체 연관 관계 (2개) *서로다른 단방향 관계를 억지로 양방향으로 부르는 것.
ㅁ 회원 -> 팀 (단방향) , 팀 -> 회원 (단방향)
A: B b , B: A a
- 테이블 연관관계 
ㅁ 회원 <-> 팀 1개 (양방향)

○연관관계의 주인 ★반드시 둘 중 하나만 지정 
-> 객체의 두 관계중 하나를 연관관계의 주인으로 지정해야 한다.  
-> 연관관계 주인만이 외래 키를 관리 (등록, 수정) 
-> 주인이 아닌 쪽은 읽기만 가능 
-> 주인은 mappedBy 속성 사용 x
-> 주인이 아니면 mappedBy 속성으로 주인 지정 

# 주인은 외래키가 있는곳으로 지정 ( 다 가 되는 곳이 주인이된다 ex) 자동차, 바퀴 에서 바퀴가 주인 )
# 양방향 매핑시에 주인에 값을 입력하지 않는 실수를 조심하자 
○ persist() : 영속화, 1차캐시에 등록 
※ 중간에 flush, clear시 1차캐시에대한 정보가 사라져서 이전에 하인에 대한 값을 주입시키지 않으면
값을 꺼내올 수가 없다. (team.getMembers().add(member);)
주입시켜두었으면 상관없이 잘 가져올 수있다.
