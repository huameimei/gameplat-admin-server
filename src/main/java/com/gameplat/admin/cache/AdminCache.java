package com.gameplat.admin.cache;

import com.gameplat.base.common.constant.RedisCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 后台账号Cache操作
 *
 * @author three
 */
@Slf4j
@Component
public class AdminCache {

  @Autowired private RedisTemplate<String, Integer> redisTemplate;

  /**
   * 获取后台登录错误次数
   *
   * @param account String
   * @return int
   */
  public int getErrorPasswordCount(String account) {
    Integer count = redisTemplate.opsForValue().get(RedisCacheKey.ADMIN_PWDERROR_KEY + account);
    return Optional.ofNullable(count).orElse(0);
  }

  public void updateErrorPasswordCount(String account, int count) {
    redisTemplate.opsForValue().increment(RedisCacheKey.ADMIN_PWDERROR_KEY + account, count);
  }

  public void cleanErrorPasswordCount(String account) {
    redisTemplate.delete(RedisCacheKey.ADMIN_PWDERROR_KEY + account);
  }
}
