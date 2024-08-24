package com.watermelon.server.event.link.service;

import com.watermelon.server.event.lottery.domain.LotteryApplier;
import com.watermelon.server.event.link.dto.MyLinkDto;

public interface LinkService {

    /**
     * 추첨 이벤트 응모자에 대한 링크를 반환.
     * @param uid 응모자의 uid
     * @return 링크 정보
     */
    MyLinkDto getShortedLink(String uid);

    LotteryApplier getApplierByLinkKey(String linkKey);

    /**
     * 단축된 uri 의 원래 uri 를 반환.
     * @param shortedUri 단축된 uri
     * @return 원래 uri
     */
    String getUrl(String shortedUri);

    String getRedirectUrl(String shortedUri);
}
