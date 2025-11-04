package com.github.natswarchuan.cache.autoconfigure;

import com.github.natswarchuan.cache.core.IDatabaseCacheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;

/**
 * Lớp cấu hình này tự động bật Scheduling và chạy cron job
 * NẾU consumer bật nó trong properties.
 */
@Configuration
@EnableScheduling 
@ConditionalOnProperty(
    prefix = "com.github.natswarchuan.cache.eviction", 
    name = "enabled", 
    havingValue = "true"
)
public class DatabaseCacheEvictionService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseCacheEvictionService.class);

    /**
     * Chúng ta định nghĩa một Bean riêng cho Task để giữ logic sạch sẽ.
     */
    @Bean
    public CacheEvictionTask cacheEvictionTask(
            IDatabaseCacheRepository<?, ?> repository, 
            DatabaseCacheProperties properties) {
        
        log.info("Database Cache Eviction: Đã bật. Cron: [{}], Max-Age: [{}].",
                properties.getEviction().getCron(),
                properties.getEviction().getMaxAge().toString());
                
        return new CacheEvictionTask(repository, properties);
    }

    /**
     * Lớp chứa logic chạy @Scheduled
     */
    public static class CacheEvictionTask {
        
        private final IDatabaseCacheRepository<?, ?> repository;
        private final DatabaseCacheProperties properties;

        public CacheEvictionTask(IDatabaseCacheRepository<?, ?> repository, DatabaseCacheProperties properties) {
            this.repository = repository;
            this.properties = properties;
        }

        @Scheduled(cron = "${com.github.natswarchuan.cache.eviction.cron}")
        public void evictOldCacheEntries() {
            log.debug("Database Cache Eviction: Đang chạy cron job dọn dẹp...");
            
            Duration maxAge = properties.getEviction().getMaxAge();
            Instant cutoff = Instant.now().minus(maxAge);

            try {
                int count = repository.evictOldEntries(cutoff);
                if (count > 0) {
                    log.info("Database Cache Eviction: Đã dọn dẹp {} entry cũ hơn {}.", count, cutoff);
                }
            } catch (Exception e) {
                log.error("Database Cache Eviction: Lỗi khi chạy cron job dọn dẹp: {}", e.getMessage());
            }
        }
    }
}
