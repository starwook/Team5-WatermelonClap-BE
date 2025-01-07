package com.watermelon.server.order.domain;



import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name="order_apply_count")
public class OrderWinningCount {
    @Id
    @Setter
    @GeneratedValue
    private Long id;

    @Setter
    private int count;

    @OneToOne
    @JoinColumn
    private OrderEvent orderEvent;

    public static OrderWinningCount create(OrderEvent orderEvent) {
        return OrderWinningCount.builder()
                .build();
    }
    public static OrderWinningCount createWithNothing(){
        return OrderWinningCount.builder().build();
    }
    public static OrderWinningCount createWithGeneratingId(long id){
        OrderWinningCount orderWinningCount = OrderWinningCount.builder().build();
        orderWinningCount.setId(id);
        return orderWinningCount;
    }

    @Builder
    public OrderWinningCount() {
        this.count =0;
    }


    public void addCountOnce(){
        this.count++;
    }
    public void addCount(int count){
        this.count +=count;
    }
    /**
     * 만약 각 ApplyCount에 정해진 개수만큼 꽉 찼다면
     * 해당 ApplyCount의 flag를 full로 만들어준다.
     */
    /**
     *정해진 갯수만큼 꽉 찼는지 판별하고 그에 따라 행동한다
     */
    public boolean tryAddCountIfUnderMax(int maxCount) {
        if (this.count < maxCount) {
            this.addCountOnce();
            return true;
        }
        return false;
    }
    public void clearCount(){
        this.count = 0;
    }
}
