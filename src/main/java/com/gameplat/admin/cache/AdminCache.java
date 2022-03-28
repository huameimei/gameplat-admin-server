package com.gameplat.admin.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 后台账号Cache操作
 *
 * @author three
 */
@Slf4j
@Component
public class AdminCache extends AbstractRedis {

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private LimitInfoService limitInfoService;

    @Autowired
    public AdminCache(RedisTemplate redisTemplate) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        this.redisTemplate = redisTemplate;
    }

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
