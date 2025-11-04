package com.github.natswarchuan.cache.core;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

/** Triển khai CacheManager. Lớp này giờ phụ thuộc vào IDatabaseCacheRepository. */
@SuppressWarnings("null") 
public class DatabaseCacheManager implements CacheManager {

  private final IDatabaseCacheRepository<? extends ICacheEntry, ?> repository;
  private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

  public DatabaseCacheManager(IDatabaseCacheRepository<? extends ICacheEntry, ?> repository) {
    this.repository = repository;
  }

  @Override
  @Nullable
  public Cache getCache(String name) {

    return this.cacheMap.computeIfAbsent(
        name, cacheName -> new DatabaseCache(cacheName, this.repository));
  }

  @Override
  public Collection<String> getCacheNames() {

    return this.repository.findDistinctCacheNames();
  }
}
