package com.gameplat.admin.config;

import com.gameplat.admin.listener.RechargeOrderExpiredListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisListenerConfig {

  @Autowired private RedisConnectionFactory factory;

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer() {
    RedisMessageListenerContainer redisMessageListenerContainer =
        new RedisMessageListenerContainer();
    redisMessageListenerContainer.setConnectionFactory(factory);
    return redisMessageListenerContainer;
  }

  @Bean
  public RechargeOrderExpiredListener keyExpiredListener() {
    return new RechargeOrderExpiredListener(this.redisMessageListenerContainer());
  }
}
