package org.dddml.ffvtraceability.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @Primary
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.SECONDS)
                .maximumSize(1000)
                .recordStats();
    }

    @Bean
    public Caffeine<Object, Object> longTermCaffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .maximumSize(1000)
                .recordStats();
    }

    @Bean
    @Primary
    public CacheManager cacheManager(@Qualifier("caffeineConfig") Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    @Bean
    public CacheManager longTermCacheManager(@Qualifier("longTermCaffeineConfig") Caffeine<Object, Object> longTermCaffeineConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(longTermCaffeineConfig);
        return cacheManager;
    }

    // 如果需要手动清除缓存，可以添加缓存清除方法：
    // @CacheEvict(value = "groupAuthorities", key = "#groupName")
    // public void evictGroupAuthorities(String groupName) {
    //     logger.debug("Evicting cache for group: {}", groupName);
    // }

    // @CacheEvict(value = "groupAuthorities", allEntries = true)
    // public void evictAllGroupAuthorities() {
    //     logger.debug("Evicting all group authorities cache");
    // }
}