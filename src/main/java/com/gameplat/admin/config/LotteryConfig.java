package com.gameplat.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "lottery")
@RefreshScope
@Data
public class LotteryConfig {
  @Value(value = "platformCode")
  private String platformCode;

  @Value(value = "proxyCode")
  private String proxyCode;

  @Value(value = "serverHost")
  private String serverHost;
}
