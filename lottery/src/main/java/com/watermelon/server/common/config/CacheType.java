package com.watermelon.server.common.config;

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
