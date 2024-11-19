package org.dddml.ffvtraceability.resource.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(100)
                .recordStats();
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    // 如果需要手动清除缓存（比如组权限发生变化时），可以添加缓存清除方法：
    // @CacheEvict(value = "groupAuthorities", key = "#groupName")
    // public void evictGroupAuthorities(String groupName) {
    //     logger.debug("Evicting cache for group: {}", groupName);
    // }

    // @CacheEvict(value = "groupAuthorities", allEntries = true)
    // public void evictAllGroupAuthorities() {
    //     logger.debug("Evicting all group authorities cache");
    // }
} 