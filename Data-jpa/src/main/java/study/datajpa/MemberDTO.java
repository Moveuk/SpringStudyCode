package study.datajpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@AllArgsConstructor
@Data
public class MemberDTO {

    private Long id;
    private String username;
    private String teamName;

    public MemberDTO(Member member) {
        id = member.getId();
        username = member.getUsername();
        teamName = member.getTeam().getName();
    }
}
