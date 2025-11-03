package com.github.natswarchuan.cache.core;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entity JPA đại diện cho một mục cache được lưu trong cơ sở dữ liệu.
 * Tên bảng có thể được cấu hình thông qua thuộc tính "com.github.natswarchuan.cache.table-name"
 * trong application.properties. Mặc định là "app_cache_entries".
 */
@Entity
@Table(name = "${com.github.natswarchuan.cache.table-name:app_cache_entries}",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_cache_name_key",
            columnNames = {"cache_name", "cache_key"}
        )
    },
    indexes = {
        @Index(name = "idx_cache_name", columnList = "cache_name"),
        @Index(name = "idx_cache_key", columnList = "cache_key")
    }
)
public class CacheEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cache_name", nullable = false, length = 100)
    private String cacheName;

    @Column(name = "cache_key", nullable = false, length = 255)
    private String cacheKey;

    @Lob
    @Column(name = "cache_value", nullable = true, columnDefinition = "BLOB")
    private byte[] value;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}

