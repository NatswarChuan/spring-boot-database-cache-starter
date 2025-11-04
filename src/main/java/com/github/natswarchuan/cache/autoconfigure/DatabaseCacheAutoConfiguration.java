package com.github.natswarchuan.cache.autoconfigure;

import com.github.natswarchuan.cache.core.DatabaseCacheManager;
import com.github.natswarchuan.cache.core.IDatabaseCacheRepository;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@AutoConfiguration
@ConditionalOnClass(CacheManager.class)
@EnableCaching
@EnableConfigurationProperties(DatabaseCacheProperties.class)
@Import(DatabaseCacheEvictionService.class)
public class DatabaseCacheAutoConfiguration {

  @Bean
  @Primary
  @ConditionalOnBean(IDatabaseCacheRepository.class)
  public CacheManager databaseCacheManager(IDatabaseCacheRepository<?, ?> repository) {
    return new DatabaseCacheManager(repository);
  }

  @Bean
  @ConditionalOnBean(DatabaseCacheProperties.class)
  public PhysicalNamingStrategy physicalNamingStrategy(DatabaseCacheProperties properties) {
    return new PhysicalNamingStrategy() {
      @Override
      public Identifier toPhysicalTableName(Identifier logicalName, JdbcEnvironment context) {

        if (logicalName.getText().equalsIgnoreCase("app_cache_entries")) {

          return Identifier.toIdentifier(properties.getTableName(), logicalName.isQuoted());
        }

        return logicalName;
      }

      @Override
      public Identifier toPhysicalCatalogName(Identifier logicalName, JdbcEnvironment context) {
        return logicalName;
      }

      @Override
      public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment context) {
        return logicalName;
      }

      @Override
      public Identifier toPhysicalSchemaName(Identifier logicalName, JdbcEnvironment context) {
        return logicalName;
      }

      @Override
      public Identifier toPhysicalSequenceName(Identifier logicalName, JdbcEnvironment context) {
        return logicalName;
      }
    };
  }
}
