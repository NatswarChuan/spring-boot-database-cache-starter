package com.github.natswarchuan.cache.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Lớp này liên kết với các thuộc tính trong application.properties
 * bắt đầu bằng "com.github.natswarchuan.cache".
 * Nó cung cấp khả năng tự động hoàn thành (auto-completion) trong IDE.
 */
@ConfigurationProperties(prefix = "com.github.natswarchuan.cache")
public class DatabaseCacheProperties {

    /**
     * Tên của bảng CSDL để lưu trữ các mục cache.
     * Mặc định là "app_cache_entries".
     */
    private String tableName = "app_cache_entries";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
