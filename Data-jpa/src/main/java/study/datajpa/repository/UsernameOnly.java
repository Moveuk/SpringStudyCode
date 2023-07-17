package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    String getUsername();

    @Value(value = "#{target.username = ' ' + target.age}") //open projection - spl 사용
    String getUsernameOP();
}
