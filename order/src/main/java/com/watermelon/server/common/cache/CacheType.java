package com.watermelon.server.common.cache;

import lombok.Getter;

@Getter
public enum CacheType {
    EXPECTATION(
            "expectations",
            60,
            100
    ),
    ORDER_EVENTS(
            "orderEvents",
            10,
            100
    ),
    TEST_FOR_EXPIRE(
            "testForExpire",1,100
    );

    CacheType(String cacheName, int expireTime, int maximumSize) {
        this.cacheName = cacheName;
        this.expireTime = expireTime;
        this.maximumSize = maximumSize;
    }

    private String cacheName;
    private int expireTime;
    private int maximumSize;
}
