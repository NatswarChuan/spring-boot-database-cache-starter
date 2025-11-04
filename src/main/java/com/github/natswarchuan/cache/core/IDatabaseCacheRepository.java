package com.github.natswarchuan.cache.core;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface (Hợp đồng) Repository LÕI.
 *
 * @param <T> Kiểu Entity, phải implement ICacheEntry
 * @param <ID> Kiểu ID của Entity (CPK)
 */
@NoRepositoryBean
public interface IDatabaseCacheRepository<T extends ICacheEntry, ID> extends JpaRepository<T, ID> {

  Optional<T> findByCacheNameAndCacheKey(String cacheName, byte[] cacheKey);

  @Transactional
  @Modifying
  void deleteByCacheNameAndCacheKey(String cacheName, byte[] cacheKey);

  @Transactional
  @Modifying
  void deleteByCacheName(String cacheName);

  @Query("SELECT DISTINCT c.cacheName FROM #{#entityName} c")
  List<String> findDistinctCacheNames();

  void upsert(String cacheName, byte[] cacheKey, byte[] value);

  @Transactional
  @Modifying
  int evictOldEntries(Instant cutoff);
}
