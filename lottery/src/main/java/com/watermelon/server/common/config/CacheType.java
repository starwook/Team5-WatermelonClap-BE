package com.watermelon.server.common.config;

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

    CacheType(String cacheName, int expireTime, int maximumSIze) {
        this.cacheName = cacheName;
        this.expireTime = expireTime;
        this.maximumSIze = maximumSIze;
    }

    private String cacheName;
    private int expireTime;
    private int maximumSIze;
}
