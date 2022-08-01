package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton() {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = annotationConfigApplicationContext.getBean(StatefulService.class);
        StatefulService statefulService2 = annotationConfigApplicationContext.getBean(StatefulService.class);

        // ThreadA: A사용자 10000원 주문
        statefulService1.order("userA", 10000);
        // ThreadA: B사용자 20000원 주문
        statefulService2.order("userB", 20000);

        // 문제 해결
        // ThreadA: A사용자 10000원 주문
        int userAPrice = statefulService1.order_solved("userA", 10000);
        // ThreadA: B사용자 20000원 주문
        int userBPrice = statefulService2.order_solved("userB", 20000);

        // ThreadA: A사용자 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        // userAPrice를 이용해 출력한다.

        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static  class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}