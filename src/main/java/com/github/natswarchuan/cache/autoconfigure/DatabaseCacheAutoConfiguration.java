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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Lớp Tự động cấu hình (AutoConfiguration) cho dependency. Lớp này sẽ tự động được kích hoạt khi dự
 * án của bạn (consumer) thêm dependency này vào.
 */
@AutoConfiguration
@ConditionalOnClass(CacheManager.class)
@EnableCaching
@EnableJpaRepositories(basePackageClasses = CacheEntryRepository.class)
@EntityScan(basePackageClasses = CacheEntry.class)
@EnableConfigurationProperties(DatabaseCacheProperties.class)
public class DatabaseCacheAutoConfiguration {

  /**
   * Tự động tạo Bean CacheManager. @ConditionalOnBean đảm bảo bean này chỉ được tạo SAU KHI Spring
   * Data JPA đã tạo thành công CacheEntryRepository.
   */
  @Bean
  @Primary
  @ConditionalOnBean(CacheEntryRepository.class)
  public CacheManager databaseCacheManager(CacheEntryRepository cacheEntryRepository) {
    return new DatabaseCacheManager(cacheEntryRepository);
  }
}
