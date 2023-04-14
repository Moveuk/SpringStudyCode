package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;

@Entity
//@SequenceGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        sequenceName = "MEMBER_SEQ",    // 매핑할 데이터 베이스 시퀀스 이름.
//        initialValue = 1, allocationSize = 1)
//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES",
//        pkColumnValue = “MEMBER_SEQ", allocationSize = 1)
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // Auto는 DB 방언에 맞추서 아래 3개 중 자동 선택
    // IDENTITY: DB 위임
    // SEQUENCE: ORACLE 주로 사용, Sequence 오브젝트를 생성해서 call하여 사용함.
    // TABLE: 키 생성 전용 테이블을 만들어 DB 시퀀스를 흉내내는 전략, 모든 DB에 적용 가능하지만 성능이 안좋을 수 있음.
    @Column(name = "MEMBER_ID")
    private Long id;
    @Column(name = "USERNAME")  //테이블 컬럼 변경
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    //    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT")
//    private List<Product> products = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    private Integer age;
    //DB에는 없는 enum 타입을 사용할 수 있게 해준다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    //날짜 타입 - BaseEntity로 상속
//    @Temporal(TemporalType.TIMESTAMP) //옵션 종류: DATA, TIME, TIMESTAMP(날짜 + 시간)
//    private Date createdDate;
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
    //최신 하이버네이트는 다음 날짜객체를 지원하여 Temporal 쓸일이 없음
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;
    @Lob    //Large Object: 문자타입의 경우 clob, 나머지는 바이트로 매핑되서 blob
    private String description;
    @Transient  //DB 컬럼으로는 만들고 싶지 않을 때 사용.(예: 메모리에서 캐시 대용으로 사용하고 싶을 때)
    private int nothing;

    //Period
    @Embedded
    private Period workPeriod;

    //Address
    @Embedded
    private Address homeAddress;
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD",
            joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();
    //    @ElementCollection
//    @CollectionTable(name = "ADDRESS",
//            joinColumns = @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    //JPA에서 생성자를 쓰려면 기본 생성자가 있어야만함.
    public Member() {
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Address workAddress) {
        this.workAddress = workAddress;
    }

    public Set<String> getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(Set<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
