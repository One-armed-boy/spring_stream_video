package com.stream.configuration;

import com.stream.storage.LocalStorageStrategy;
import com.stream.storage.StorageStrategy;
import com.stream.storage.exception.InvalidStorageConfigException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
  @Value("${my.app.storage.type}")
  private String targetStorage;

  @Bean
  public StorageStrategy storageStrategy() {
    switch (targetStorage) {
      case "local": {
        return new LocalStorageStrategy();
      }
      default: {
        throw new InvalidStorageConfigException(targetStorage);
      }
    }
  }
}
