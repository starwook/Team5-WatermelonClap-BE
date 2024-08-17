package com.watermelon.server.common.cache;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Getter
@Service
public class CacheService {
    private final CacheManager cacheManager;
    private final String OrderEventKey = "orderEventKey";

    public void putCache(String cacheName,String key, Object value){
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(key, value);
    }
    public Object getCacheValueByObject(String cacheName, String key){
        return cacheManager.getCache(cacheName).get(key).get();
    }
}
