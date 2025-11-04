package com.github.natswarchuan.cache.core;

import java.time.Instant;

/** Interface (Hợp đồng) cho một thực thể (Entity) cache. */
public interface ICacheEntry {

  String getCacheName();

  void setCacheName(String cacheName);

  byte[] getCacheKey();

  void setCacheKey(byte[] cacheKey);

  byte[] getValue();

  void setValue(byte[] value);

  Instant getCreatedAt();

  void setCreatedAt(Instant createdAt);
}
