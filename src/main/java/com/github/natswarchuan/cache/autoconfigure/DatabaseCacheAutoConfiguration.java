package com.github.natswarchuan.cache.autoconfigure;

import com.github.natswarchuan.cache.core.CacheEntry;
import com.github.natswarchuan.cache.core.CacheEntryRepository;
import com.github.natswarchuan.cache.core.DatabaseCacheManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Lớp Tự động cấu hình (AutoConfiguration) cho dependency. CHỈ quét Entity (@EntityScan), không
 * quét Repository (@EnableJpaRepositories) để tránh xung đột và ghi đè cấu hình của Consumer.
 */
@AutoConfiguration
@ConditionalOnClass(CacheManager.class)
@EnableCaching
@EntityScan(basePackageClasses = CacheEntry.class)
@EnableConfigurationProperties(DatabaseCacheProperties.class)
public class DatabaseCacheAutoConfiguration {

  /**
   * Tự động tạo Bean CacheManager. @ConditionalOnBean đảm bảo bean này chỉ được tạo SAU KHI dự án
   * Consumer (người dùng) đã chủ động quét và tạo ra CacheEntryRepository.
   */
  @Bean
  @Primary
  @ConditionalOnBean(CacheEntryRepository.class)
  public CacheManager databaseCacheManager(CacheEntryRepository cacheEntryRepository) {
    return new DatabaseCacheManager(cacheEntryRepository);
  }
}
