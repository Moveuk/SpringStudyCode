package hello.core.singleton;

public class StatefulService {

    private int price; // 상태를 유지하는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; // 여기가 문제! 10000 -> 20000
    }

    // 해결하기 위해선 전역변수가 아닌 지역변수로 바꿔주면 된다.
    public int order_solved(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price; // 여기가 문제! 10000 -> 20000
    }

    public int getPrice() {
        return price;
    }
}
