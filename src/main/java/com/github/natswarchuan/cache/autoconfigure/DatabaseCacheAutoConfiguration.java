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
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@AutoConfiguration
@ConditionalOnClass(CacheManager.class)
@EnableCaching
@EntityScan(basePackageClasses = CacheEntry.class)
@EnableConfigurationProperties(DatabaseCacheProperties.class)
@Import(JpaStarterConfig.class)
public class DatabaseCacheAutoConfiguration {

  @Bean
  @Primary
  @ConditionalOnBean(CacheEntryRepository.class)
  public CacheManager databaseCacheManager(CacheEntryRepository cacheEntryRepository) {
    return new DatabaseCacheManager(cacheEntryRepository);
  }
}
