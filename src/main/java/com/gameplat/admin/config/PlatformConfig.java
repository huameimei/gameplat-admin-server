package com.gameplat.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "platform")
@RefreshScope
public class PlatformConfig {
  @Value(value = "customCode")
  private String customCode;

  @Value(value = "centerHost")
  private String centerHost;

  @Value(value = "localFilePath")
  private String localFilePath;
}
