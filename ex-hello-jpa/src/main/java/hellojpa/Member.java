package hellojpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;
    @Column(name = "name")  //테이블 컬럼 변경
    private String username;
    private Integer age;
    //DB에는 없는 enum 타입을 사용할 수 있게 해준다.
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    //날짜 타입
    @Temporal(TemporalType.TIMESTAMP) //옵션 종류: DATA, TIME, TIMESTAMP(날짜 + 시간)
    private Date createdDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;
    //최신 하이버네이트는 다음 날짜객체를 지원하여 Temporal 쓸일이 없음
    private LocalDate testLocalDate;
    private LocalDateTime testLocalDateTime;
    @Lob    //Large Object: 문자타입의 경우 clob, 나머지는 바이트로 매핑되서 blob
    private String description;
    @Transient  //DB 컬럼으로는 만들고 싶지 않을 때 사용.(예: 메모리에서 캐시 대용으로 사용하고 싶을 때)
    private int nothing;

    //JPA에서 생성자를 쓰려면 기본 생성자가 있어야만함.
    public Member() {
    }
}
