package com.github.natswarchuan.cache.core;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Triển khai interface CacheManager của Spring.
 * Lớp này chịu trách nhiệm tạo và quản lý các 'DatabaseCache' (ví dụ: một cho "products", một cho "users").
 */
public class DatabaseCacheManager implements CacheManager {

    private final CacheEntryRepository repository;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

    public DatabaseCacheManager(CacheEntryRepository repository) {
        this.repository = repository;
    }

    /**
     * Lấy một cache instance theo tên.
     * Nếu chưa tồn tại, một 'DatabaseCache' mới sẽ được tạo và lưu lại.
     */
    @Override
    @Nullable
    public Cache getCache(String name) {
        return this.cacheMap.computeIfAbsent(name, cacheName -> 
            new DatabaseCache(cacheName, this.repository)
        );
    }

    /**
     * Lấy danh sách tên của tất cả các cache đang được quản lý.
     */
    @Override
    public Collection<String> getCacheNames() {
        // Lấy danh sách tên cache trực tiếp từ CSDL
        return this.repository.findDistinctCacheNames();
    }
}
