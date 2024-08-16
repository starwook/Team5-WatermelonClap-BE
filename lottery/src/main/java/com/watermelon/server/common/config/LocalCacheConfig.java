package com.watermelon.server.common.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.boot.actuate.metrics.cache.CaffeineCacheMeterBinderProvider;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class LocalCacheConfig  {
    private final CacheMetricsRegistrar cacheMetricsRegistrar;

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(),
                                Caffeine.newBuilder().recordStats()
                                        .expireAfterWrite(cache.getExpireTime(), TimeUnit.SECONDS)
                                        .maximumSize(cache.getMaximumSize())
                                        /*.recordStats()*/
                                        .build()
                        )
                )
                .collect(Collectors.toList());

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
