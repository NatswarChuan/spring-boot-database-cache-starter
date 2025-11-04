package com.github.natswarchuan.cache.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.time.Duration;

/**
 * Lớp này liên kết với các thuộc tính trong application.properties
 * Sửa đổi: Thêm cấu hình cho Eviction (dọn dẹp cache)
 */
@ConfigurationProperties(prefix = "com.github.natswarchuan.cache")
public class DatabaseCacheProperties {

    private String tableName = "app_cache_entries";

    /**
     * Thêm cấu hình lồng nhau cho việc dọn dẹp cache
     */
    private Eviction eviction = new Eviction();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Eviction getEviction() {
        return eviction;
    }

    public void setEviction(Eviction eviction) {
        this.eviction = eviction;
    }

    /**
     * Lớp con chứa các thuộc tính cho cron job dọn dẹp cache
     */
    public static class Eviction {

        /**
         * Bật/tắt cron job dọn dẹp. Mặc định là false.
         */
        private boolean enabled = false;

        /**
         * Biểu thức cron cho việc dọn dẹp.
         * Mặc định: 1 giờ sáng mỗi ngày.
         */
        private String cron = "0 0 1 * * ?";

        /**
         * Tuổi tối đa của một entry cache.
         * Bất kỳ entry nào cũ hơn 'max-age' sẽ bị xóa.
         * Mặc định: 7 ngày.
         * (Định dạng: PnDTnHnMn.nS, ví dụ: PT10M = 10 phút, P7D = 7 ngày)
         */
        private Duration maxAge = Duration.ofDays(7);

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }

        public Duration getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(Duration maxAge) {
            this.maxAge = maxAge;
        }
    }
}
