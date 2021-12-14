package com.gameplat.admin.service.game.config;

import feign.Logger;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;

public class FeignRestConfig {

  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .connectionPool(new ConnectionPool())
        .build();
  }

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
