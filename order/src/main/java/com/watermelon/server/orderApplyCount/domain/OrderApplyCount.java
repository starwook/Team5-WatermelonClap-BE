package com.watermelon.server.orderApplyCount.domain;



import com.watermelon.server.order.domain.OrderEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name="order_apply_count")
public class OrderApplyCount {
    @Id
    @Setter
    @GeneratedValue
    private Long id;

    @Setter
    private int count;

    boolean isFull;

    public static OrderApplyCount create(OrderEvent orderEvent) {
        return OrderApplyCount.builder()
                .build();
    }
    public static OrderApplyCount createWithNothing(){
        return OrderApplyCount.builder().build();
    }
    public static OrderApplyCount createWithGeneratingId(long id){
        OrderApplyCount orderApplyCount = OrderApplyCount.builder().build();
        orderApplyCount.setId(id);
        return orderApplyCount;

    }

    @Builder
    public OrderApplyCount() {
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
    public void isCountMaxThenMakeFull(int maxCount){
        if(maxCount <= this.count){ this.makeFull();}
    }

    /**
     *정해진 갯수만큼 꽉 찼는지 판별하고 그에 따라 행동한다
     */
    public boolean tryAddCountIfUnderMax(int maxCount) {
        if (this.count < maxCount) {
            this.addCountOnce();
            this.isCountMaxThenMakeFull(maxCount);
            return true;
        }
        return false;
    }


    public void clearCount(){
        this.count = 0;
        this.isFull = false;
    }
    public void makeFull(){
        this.isFull = true;
    }
    public void makeNotFull(){
        this.isFull = false;
    }
}
