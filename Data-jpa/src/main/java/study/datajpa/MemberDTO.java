package study.datajpa;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemberDTO {

    private Long id;
    private String username;
    private String teamName;
}
