package com.watermelon.server.event.lottery.service;

import com.watermelon.server.admin.dto.response.ResponseLotteryApplierDto;
import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.lottery.dto.response.ResponseLotteryRankDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LotteryService {

    /**
     * uid 를 가진 유저에 대한 추첨 이벤트 순위를 반환한다.
     * @param uid
     * @return 순위
     */
    ResponseLotteryRankDto getLotteryRank(String uid);

    /**
     * 응모한 뒤 응모자 객체를 반환. 만약 처음 응모라면, 응모 처리.
     * @param uid
     * @return 응모자
     */
    LotteryApplier applyAndGet(String uid);

    /**
     * uid 를 가진 응모자에 대한 남은 뽑기 횟수를 반환한다.
     * @param uid uid
     * @return 뽑기 횟수
     */
    int getRemainChance(String uid);

    /**
     * 응모자 정보를 페이지로 반환
     * @param pageable 페이지 정보
     * @return 응모자 정보 페이지
     */
    Page<ResponseLotteryApplierDto> getApplierInfoPage(Pageable pageable);

    /**
     * 추첨 이벤트 응모 인원에 대해 뽑기를 진행한다.
     */
    void lottery();


    /*외부에서 LotteryApplier를 찾아오려고 할 떄 사용해야하는 메소드*/
    LotteryApplier findLotteryApplierByUid(String uid);

    /**
     * 처음 로그인했는지 판별한다.
     * 만약 처음 로그인이라면 회원가입하고, 링크 uri 가 있다면 뽑기권을 증가시킨다.
     * @param uid
     */
    void firstLogin(String uid, String uri);

}
