package com.gameplat.admin.cache;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.OnlineCount;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.common.constant.RedisCacheKey;
import com.gameplat.common.util.StringUtils;
import com.gameplat.redis.api.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 后台账号Cache操作
 *
 * @author three
 */
@Slf4j
@Component
public class AdminCache {

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Autowired private RedisClient redisClient;

  // 在线用户过期30分钟
  public static final long ONLINE_EXPIRE = 1800;

  /** 初始化 */
  @PostConstruct
  public void init() {}

  /**
   * 设置后台登录错误次数
   *
   * @param account
   * @param count
   * @return
   */
  public void setErrorPasswordCount(String account, Integer count) {
    // 24小时
    redisTemplate
        .boundValueOps(RedisCacheKey.ADMIN_PWDERROR_KEY + account)
        .set(count, 60 * 60 * 24, TimeUnit.SECONDS);
  }

  /**
   * 获取后台登录错误次数
   *
   * @param account
   * @return
   */
  public int getErrorPasswordCount(String account) {
    Object obj = redisTemplate.boundValueOps(RedisCacheKey.ADMIN_PWDERROR_KEY + account).get();
    if (StringUtils.isNull(obj)) {
      return 0;
    }
    return (Integer) obj;
  }

  public void cleanErrorPasswordCount(String account) {
    redisTemplate.delete(RedisCacheKey.ADMIN_PWDERROR_KEY + account);
  }

  /**
   * 获取在线总人数(排除总控)
   *
   * @return
   */
  public int getOnlineCount() {
    Set<String> keys = redisTemplate.keys(RedisCacheKey.PREFIX_ADMIN_SECURITY_CACHE + "*");
    keys.addAll(redisTemplate.keys(RedisCacheKey.PREFIX_AGENT_SECURITY_CACHE + "*"));
    keys.addAll(redisTemplate.keys(RedisCacheKey.PREFIX_Client_SECURITY_CACHE + "*"));
    return keys.size();
  }

  /**
   * 获取在线用户表
   *
   * @param userDTO
   * @return
   */
  public OnlineUserVo getOnlineList(PageDTO<UserToken> page, OnlineUserDTO userDTO) {
   return null;
  }

  public static boolean isEqualKeywordList(String str, List<String> keywordList) {
    return keywordList.stream().anyMatch(str::equalsIgnoreCase);
  }

  /**
   * 后台根据类型获取在线人数
   *
   * @return
   */
  public Map<String, Integer> getOnlineUserCounts() {
    return null;
  }

  public static void main(String[] args) {
    OnlineCount count = new OnlineCount();
    int num = count.getWindowsCount() + 1;
    count.setWindowsCount(num);
    System.out.println(count);
  }
}
