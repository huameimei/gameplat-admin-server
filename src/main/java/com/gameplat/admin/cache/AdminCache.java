package com.gameplat.admin.cache;

import com.gameplat.admin.enums.AdminTypeEnum;
import com.gameplat.admin.model.bean.TokenInfo;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.service.SysDictDataService;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

/** 管理员cache */
@Repository
public class AdminCache {
  @Resource private RedisTemplate<String, Object> redisTemplate;

  @Resource private SysDictDataService sysDictDataService;

  private long loginExpiredTime = 6 * 3600;

  public static final String TOKEN_INFO_ADMIN = "tokenInfo_admin_"; // 管理员
  public static final String TOKEN_INFO_HY = "tokenInfo_user_"; // 会员

  public static final String ONLINE_ADMIN = "online_admin_"; // 管理员
  public static final String ONLINE_HY = "online_user_"; // 会员

  /** admin密码错误次数 */
  public static final String ADMIN_PWDERROR_KEY = "ADMIN_PWDERROR_KEY_";
  //	public static final long LOGIN_EXPIRE = 24*3600; //登录状态过期时间   24小时
  public static final long ONLINE_EXPIRE = 1800; // 在线用户   30分钟

  @PostConstruct
  public void init() {
    SysDictData tokenExpiredTime =
        sysDictDataService.getDictValue("SYSTEM_CONFIG", "token_expired_time");
    long expiredTime = Optional.ofNullable(tokenExpiredTime.getDictValue()).isPresent() ? Long.parseLong(tokenExpiredTime.getDictValue()) : 6;
    loginExpiredTime = expiredTime * 3600;
  }

  /**
   * 登录用户是否存在
   *
   * @param uid
   * @param type
   * @return
   */
  public boolean exists(long uid, Integer type) {
    String key = TOKEN_INFO_HY;
    if (AdminTypeEnum.isAdmin(type)) {
      key = TOKEN_INFO_ADMIN;
    }
    Boolean exists = redisTemplate.hasKey(key + uid);
    if (exists == null) {
      return false;
    }
    return exists;
  }

  /**
   * 在线用户是否存在
   *
   * @param uid
   * @param type
   * @return
   */
  public boolean existsOnline(long uid, Integer type) {
    String key = ONLINE_HY;
    if (AdminTypeEnum.isAdmin(type)) {
      key = ONLINE_ADMIN;
    }
    Boolean exists = redisTemplate.hasKey(key + uid);
    if (exists == null) {
      return false;
    }
    return exists;
  }

  public void updateAdminTokenExpire(Long uid) {
    String tokenKey = TOKEN_INFO_ADMIN + uid;
    if (redisTemplate.hasKey(tokenKey)) {
      redisTemplate.expire(tokenKey, 3, TimeUnit.HOURS);
      updateOnlineInfo(ONLINE_ADMIN + uid, tokenKey);
    }
  }

  public void updateUserTokenExpire(Long uid) {
    String tokenKey = TOKEN_INFO_HY + uid;
    if (redisTemplate.hasKey(tokenKey)) {
      redisTemplate.expire(tokenKey, loginExpiredTime, TimeUnit.SECONDS);
      updateOnlineInfo(ONLINE_HY + uid, tokenKey);
    }
  }

  public void updateOnlineInfo(String onlineKey, String tokenKey) {
    if (redisTemplate.hasKey(onlineKey)) {
      redisTemplate.expire(onlineKey, ONLINE_EXPIRE, TimeUnit.SECONDS);
    } else {
      TokenInfo info = (TokenInfo) redisTemplate.opsForValue().get(tokenKey);
      redisTemplate.opsForValue().set(onlineKey, info, ONLINE_EXPIRE, TimeUnit.SECONDS);
    }
  }
  /**
   * 获取会员token
   *
   * @param uid
   * @return
   */
  public TokenInfo getHYTokenInfo(Long uid) {
    String key = TOKEN_INFO_HY + uid;
    return (TokenInfo) this.redisTemplate.boundValueOps(key).get();
  }

  /**
   * 获取admin token
   *
   * @param uid
   * @return
   */
  public TokenInfo getAdminTokenInfo(Long uid) {
    String key = TOKEN_INFO_ADMIN + uid;
    return (TokenInfo) this.redisTemplate.boundValueOps(key).get();
  }

  /**
   * 获取在线总人数
   *
   * @return
   */
  public int getOnlineCount() {
    Set<String> keys = redisTemplate.keys(ONLINE_HY + "*");
    keys.addAll(redisTemplate.keys(ONLINE_ADMIN + "*"));
    return keys.size();
  }

  /**
   * 密码错误次数
   *
   * @param uid
   * @param num
   */
  public void setErrorPwdNum(Long uid, int num) {
    redisTemplate.boundValueOps(ADMIN_PWDERROR_KEY + uid).set(num, 60 * 60 * 24, TimeUnit.SECONDS);
  }

  /**
   * 删除
   *
   * @param uid
   */
  public void delErrorPwdNum(Long uid) {
    redisTemplate.delete(ADMIN_PWDERROR_KEY + uid);
  }

  /**
   * 获取密码错误次数
   *
   * @param uid
   * @return
   */
  public int getErrorPwdNum(Long uid) {
    Object obj = redisTemplate.boundValueOps(ADMIN_PWDERROR_KEY + uid).get();
    if (obj == null) {
      return 0;
    }
    return (Integer) obj;
  }

  /** 清除角色权限 */
  public void clearRolePrivileges() {
    if (this.clearWithPattern("privilege_admin*") > 0) {
      redisTemplate.delete("menu_findAllMenu");
    }
  }

  /**
   * 删除Redis
   *
   * @param pattern
   */
  public int clearWithPattern(String pattern) {
    Set<String> keys = redisTemplate.keys(pattern);
    if (CollectionUtils.isNotEmpty(keys)) {
      redisTemplate.delete(keys);
      return keys.size();
    } else {
      return 0;
    }
  }
}
