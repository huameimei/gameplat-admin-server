package com.gameplat.admin.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration
@RefreshScope
@Data
public class SysTheme {
  @Value(value = "${tenant}")
  private String tenantCode;
}
