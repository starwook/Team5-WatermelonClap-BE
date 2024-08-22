package com.watermelon.server.order.result.domain;


import com.watermelon.server.order.domain.OrderEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
public class OrderApplyCount {
    @Id
    @GeneratedValue
    private Long id;

    @Setter
    private int count;
//

    public static OrderApplyCount create(OrderEvent orderEvent) {
        return OrderApplyCount.builder()
                .build();
    }
    public static OrderApplyCount createWithNothing(){
        return OrderApplyCount.builder().build();
    }

    @Builder
    public OrderApplyCount() {
        this.count =0;
    }
    public void addCount(){
        this.count++;
    }

    public void clearCount(){
        this.count = 0;
    }
}
