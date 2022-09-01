package com.gameplat.admin.cache;

import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 后台账号Cache操作
 *
 * @author three
 */
@Slf4j
@Component("AdminCache")
public class AdminCache extends AbstractRedis {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private LimitInfoService limitInfoService;

    @Override
    public RedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }

    /**
     * 获取后台登录错误次数
     *
     * @param account String
     * @return int
     */
    public int getErrorPasswordCount(String account) {
        Integer count = redisTemplate.opsForValue().get(this.getPwdErrorCountKey(account));
        return Optional.ofNullable(count).orElse(0);
    }

    public void updateErrorPasswordCount(String account) {
        AdminLoginLimit limit = limitInfoService.getAdminLimit();
        Integer pwdErrorCount = limit.getPwdErrorCount();
        if (null == pwdErrorCount || pwdErrorCount <= 0) {
            return;
        }

        // 更新密码错误次数
        String key = this.getPwdErrorCountKey(account);
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
    }

    public void cleanErrorPasswordCount(String account) {
        redisTemplate.delete(this.getPwdErrorCountKey(account));
    }

    private String getPwdErrorCountKey(String account) {
        return String.format(CachedKeys.ADMIN_PWD_ERROR_COUNT, account);
    }
}
