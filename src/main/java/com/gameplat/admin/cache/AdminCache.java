package com.gameplat.admin.cache;

import com.gameplat.common.constant.RedisCacheKey;
import com.gameplat.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 后台账号Cache操作
 *
 * @author three
 */
@Slf4j
@Component
public class AdminCache {

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  /**
   * 获取后台登录错误次数
   *
   * @param account String
   * @return int
   */
  public int getErrorPasswordCount(String account) {
    Object obj = redisTemplate.opsForValue().get(RedisCacheKey.ADMIN_PWDERROR_KEY + account);
    return StringUtils.isNull(obj) ? 0 : (Integer) obj;
  }

  public void cleanErrorPasswordCount(String account) {
    redisTemplate.delete(RedisCacheKey.ADMIN_PWDERROR_KEY + account);
  }
}
