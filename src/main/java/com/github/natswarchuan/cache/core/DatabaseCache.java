package com.github.natswarchuan.cache.core;

import java.io.*;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Triển khai interface Cache của Spring. THAY ĐỔI LỚN: Giờ chúng ta serialize() Object key thay vì
 * key.toString().
 */
@SuppressWarnings("null")
public class DatabaseCache implements Cache {

  private static final Logger log = LoggerFactory.getLogger(DatabaseCache.class);

  private final String name;
  private final IDatabaseCacheRepository<?, ?> repository;

  public DatabaseCache(String name, IDatabaseCacheRepository<?, ?> repository) {
    Assert.notNull(name, "Tên Cache không được null");
    Assert.notNull(repository, "Repository không được null");
    this.name = name;
    this.repository = repository;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Object getNativeCache() {
    return this.repository;
  }

  /** Lấy giá trị từ cache. */
  @Override
  @Nullable
  public ValueWrapper get(Object key) {

    byte[] keyBytes = serialize(key);
    log.trace("Cache GET: [Cache: {}, Key: (binary)]", name);

    return repository
        .findByCacheNameAndCacheKey(name, keyBytes)
        .map(
            entry -> {
              Object value = deserialize(entry.getValue());
              return (value != null ? new SimpleValueWrapper(value) : null);
            })
        .orElse(null);
  }

  @Override
  @Nullable
  public <T> T get(Object key, @Nullable Class<T> type) {
    ValueWrapper wrapper = get(key);
    if (wrapper == null) {
      return null;
    }
    Object value = wrapper.get();
    if (value != null && type != null && !type.isInstance(value)) {
      throw new IllegalStateException(
          "Cached value is not of required type [" + type.getName() + "]: " + value);
    }
    @SuppressWarnings("unchecked")
    T result = (T) value;
    return result;
  }

  @Override
  @Nullable
  public <T> T get(Object key, Callable<T> valueLoader) {
    ValueWrapper wrapper = get(key);
    if (wrapper != null) {
      @SuppressWarnings("unchecked")
      T value = (T) wrapper.get();
      return value;
    }

    synchronized (this) {
      wrapper = get(key);
      if (wrapper != null) {
        @SuppressWarnings("unchecked")
        T value = (T) wrapper.get();
        return value;
      }

      T value;
      try {
        value = valueLoader.call();
      } catch (Exception e) {
        throw new ValueRetrievalException(key, valueLoader, e);
      }
      this.put(key, value);
      return value;
    }
  }

  /** Lưu giá trị vào cache. */
  @Override
  public void put(Object key, @Nullable Object value) {

    byte[] keyBytes = serialize(key);
    byte[] valueBytes = serialize(value);
    log.trace("Cache PUT: [Cache: {}, Key: (binary)]", name);

    repository.upsert(name, keyBytes, valueBytes);
  }

  /** Xóa một mục khỏi cache. */
  @Override
  public void evict(Object key) {

    byte[] keyBytes = serialize(key);
    log.trace("Cache EVICT: [Cache: {}, Key: (binary)]", name);
    repository.deleteByCacheNameAndCacheKey(name, keyBytes);
  }

  /** Xóa tất cả các mục trong cache này. */
  @Override
  public void clear() {
    log.trace("Cache CLEAR: [Cache: {}]", name);
    repository.deleteByCacheName(name);
  }

  /** Chuyển đổi đối tượng Java (phải implements Serializable) thành byte[]. (Không thay đổi) */
  private byte[] serialize(@Nullable Object obj) {
    if (obj == null) {

      return new byte[0];
    }
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(obj);
      return bos.toByteArray();
    } catch (IOException e) {

      throw new CacheSerializationException(
          "Lỗi khi serialize cache object. Class: " + obj.getClass().getName(), e);
    }
  }

  /** Chuyển đổi byte[] (từ DB) trở lại đối tượng Java. (Không thay đổi) */
  @Nullable
  private Object deserialize(@Nullable byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis)) {
      return ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new CacheSerializationException("Lỗi khi deserialize cache value", e);
    }
  }

  /** Ngoại lệ tùy chỉnh. (Không thay đổi) */
  private static class CacheSerializationException extends RuntimeException {
    public CacheSerializationException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}
