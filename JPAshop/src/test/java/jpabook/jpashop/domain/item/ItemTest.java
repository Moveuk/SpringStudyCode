package jpabook.jpashop.domain.item;

import jpabook.jpashop.exception.NotEnoughStockException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemTest {

    @Test(expected = NotEnoughStockException.class)
    public void 재고_수량_감소() {
        //when
        Item item = new Book();
        item.setName("테스트 책");
        item.setPrice(10000);
        item.setStockQuantity(10);

        //then
        item.removeStock(11);

        //given
        fail("재고 수량 부족으로 인한 예외가 발생해야한다.");
    }
}