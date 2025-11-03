package com.github.natswarchuan.cache.autoconfigure;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Lớp cấu hình này được @Import bởi DatabaseCacheAutoConfiguration để kích hoạt quét JPA cho
 * package của starter mà không ghi đè cơ chế quét mặc định của consumer.
 */
@EnableJpaRepositories("com.github.natswarchuan.cache.core")
public class JpaStarterConfig {}
