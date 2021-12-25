package com.gameplat.admin.config;

import com.gameplat.common.compent.kaptcha.KaptchaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/** KAPTCHA 验证码配置 */
@Configuration
public class KaptchaConfig {

  @Autowired RedisTemplate<String, Object> redisTemplate;

  @Bean
  public KaptchaProducer kaptchaProducer() {
    return new KaptchaProducer(redisTemplate);
  }
}
