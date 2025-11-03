package com.github.natswarchuan.cache.core;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/** Spring Data JPA Repository cho entity CacheEntry. */
@Repository
public interface CacheEntryRepository extends JpaRepository<CacheEntry, Long> {

  /** Tìm một mục cache dựa trên tên cache và khóa cache. */
  Optional<CacheEntry> findByCacheNameAndCacheKey(String cacheName, String cacheKey);

  /** Xóa một mục cache dựa trên tên cache và khóa cache. */
  @Transactional
  @Modifying
  void deleteByCacheNameAndCacheKey(String cacheName, String cacheKey);

  /** Xóa tất cả các mục cache thuộc về một tên cache (ví dụ: clear "products"). */
  @Transactional
  @Modifying
  void deleteByCacheName(String cacheName);

  /** Lấy danh sách tất cả các tên cache_name duy nhất. */
  @Query("SELECT DISTINCT c.cacheName FROM CacheEntry c")
  List<String> findDistinctCacheNames();

  /**
   * Phương thức 'Update or Insert' (Upsert) tùy chỉnh. Nó tìm một entry. Nếu tồn tại, nó cập nhật
   * 'value'. Nếu không, nó tạo một entry mới. Sử dụng @Transactional để đảm bảo tính toàn vẹn.
   */
  @Transactional
  default void upsert(String cacheName, String cacheKey, byte[] value) {
    CacheEntry entry = findByCacheNameAndCacheKey(cacheName, cacheKey).orElse(new CacheEntry());

    entry.setCacheName(cacheName);
    entry.setCacheKey(cacheKey);
    entry.setValue(value);

    save(entry);
  }
}
