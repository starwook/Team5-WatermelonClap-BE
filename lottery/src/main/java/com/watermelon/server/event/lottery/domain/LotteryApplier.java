package com.watermelon.server.event.lottery.domain;

import com.watermelon.server.BaseEntity;
import com.watermelon.server.BaseEntityListener;
import com.watermelon.server.event.parts.domain.LotteryApplierParts;
import com.watermelon.server.event.parts.exception.PartsDrawLimitExceededException;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@EntityListeners(BaseEntityListener.class)
//@ToString
public class LotteryApplier extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    @Builder.Default
    private boolean isPartsWinner = false;

    @Builder.Default
    private int lotteryRank = -1;

    @Builder.Default
    private boolean isPartsApplier = false;

    @Builder.Default
    private boolean isLotteryApplier = false;

    @Builder.Default
    private int remainChance = 3;

    private String email;

    private String phoneNumber;

    private String name;

    private String address;

    @Builder.Default
    private boolean isLotteryWinnerCheckedByAdmin = false;

    @Builder.Default
    private boolean isPartsWinnerCheckedByAdmin = false;

    @OneToOne(mappedBy = "lotteryApplier", cascade = CascadeType.ALL)
    private Link link;

    @OneToOne(mappedBy = "lotteryApplier")
    private Expectation expectation;

    @OneToMany(mappedBy = "lotteryApplier")
    private List<LotteryApplierParts> lotteryApplierParts;

    @Version
    private Long version;


    public void addNewExpectation(Expectation expectation) {
        this.expectation = expectation;
    }
    public void setLotteryWinnerInfo(String address, String name, String phoneNumber){
        this.address = address;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void applyLottery(){
        this.isLotteryApplier = true;
    }

    public void lotteryWinnerCheck(){
        this.isLotteryWinnerCheckedByAdmin = true;
    }

    public void partsWinnerCheck(){
        this.isPartsWinnerCheckedByAdmin = true;
    }

    public void lotteryWin(LotteryReward reward, String email) {
        this.email = email;
        this.lotteryRank = reward.getLotteryRank();
    }

    public void partsLotteryWin(String email){
        this.email = email;
        this.isPartsWinner = true;
    }

    public static LotteryApplier createTestLotteryWinner(String uid){
        return LotteryApplier.builder()
                .uid(uid)
                .lotteryRank(1)
                .name("name")
                .email("email")
                .address("address")
                .phoneNumber("phoneNumber")
                .isLotteryApplier(true)
                .build();
    }

    public static LotteryApplier createLotteryApplier(String uid){

        LotteryApplier lotteryApplier = LotteryApplier.builder()
                .uid(uid)
                .build();

        lotteryApplier.link = Link.createLink(lotteryApplier);

        return lotteryApplier;
    }

    /**
     * 파츠 추첨 응모
     */
    public void applyPartsLottery(){
        this.isPartsApplier = true;
    }

    public void drawParts(){
        if(remainChance == 0) throw new PartsDrawLimitExceededException();
        this.remainChance--;
    }

    public static LotteryApplier createHasNoRemainChance(String uid){
        return LotteryApplier.builder()
                .uid(uid)
                .remainChance(0)
                .build();
    }

    public void addRemainChance(){
        this.remainChance++;
    }

}
