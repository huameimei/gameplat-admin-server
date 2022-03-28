package com.gameplat.admin.cache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * spring redis 工具类
 **/
public abstract class AbstractRedis {

    public abstract RedisTemplate getRedisTemplate();

    public boolean hasKey(String key) {
        return getRedisTemplate().hasKey(key);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value) {
        ValueOperations<String, T> operation = getRedisTemplate().opsForValue();
        operation.set(key, value);
        return operation;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  缓存存活时间
     * @param timeUnit 时间颗粒度
     * @return 缓存的对象
     */
    public <T> ValueOperations<String, T> setCacheObject(String key, T value, Integer timeout, TimeUnit timeUnit) {
        ValueOperations<String, T> operation = getRedisTemplate().opsForValue();
        operation.set(key, value, timeout, timeUnit);
        return operation;
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(String key) {
        ValueOperations<String, T> operation = getRedisTemplate().opsForValue();
        return operation.get(key);
    }

    public <T> T changeCacheObject(String key, T value) {
        ValueOperations<String, T> operation = getRedisTemplate().opsForValue();
        return operation.getAndSet(key, value);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(String key) {
        return getRedisTemplate().delete(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public void deleteKey(String key) {
        getRedisTemplate().opsForHash().delete(key);
    }

    /**
     * 模糊删除多个对象
     *
     * @param prefixKey
     */
    public void deleteByPrefix(String prefixKey) {
        Collection<String> keys = getRedisTemplate().keys(prefixKey);
        getRedisTemplate().delete(keys);

    }

    /**
     * 删除集合对象
     *
     * @param collection
     */
    public void deleteObject(Collection collection) {
        getRedisTemplate().delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> ListOperations<String, T> setCacheList(String key, List<T> dataList) {
        ListOperations listOperation = getRedisTemplate().opsForList();
        if (null != dataList) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                listOperation.leftPush(key, dataList.get(i));
            }
        }
        return listOperation;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(String key) {
        List<T> dataList = new ArrayList<T>();
        ListOperations<String, T> listOperation = getRedisTemplate().opsForList();
        Long size = listOperation.size(key);

        for (int i = 0; i < size; i++) {
            dataList.add(listOperation.index(key, i));
        }
        return dataList;
    }

    public long removeByCacheList(String key, Object v) {
        return getRedisTemplate().opsForList().remove(key, 0, v);
    }

    public long addByCacheList(String key, Object v) {
        return getRedisTemplate().opsForList().leftPush(key, v);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = getRedisTemplate().boundSetOps(key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 向set集合中添加一条元素
     */
    public void addSet(String redisKey, Object obj) {
        getRedisTemplate().opsForSet().add(redisKey, obj);
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(String key) {
        Set<T> dataSet = new HashSet<T>();
        BoundSetOperations<String, T> operation = getRedisTemplate().boundSetOps(key);
        dataSet = operation.members();
        return dataSet;
    }

    public Boolean setKeyExperice(String key, Long time, TimeUnit timeUnit) {
        return getRedisTemplate().expire(key, time, timeUnit);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     * @return
     */
    public <T> HashOperations<String, String, T> setCacheMap(String key, Map<String, T> dataMap, Integer timeout, TimeUnit timeUnit) {
        HashOperations hashOperations = getRedisTemplate().opsForHash();
        if (null != dataMap) {
            for (Map.Entry<String, T> entry : dataMap.entrySet()) {
                hashOperations.put(key, entry.getKey(), entry.getValue());
            }
        }
        setKeyExperice(key, timeout.longValue(), timeUnit);
        return hashOperations;
    }

    /**
     * 获得锁
     */
    public boolean getLock(String lockId, long millisecond) {
        Boolean success = getRedisTemplate().opsForValue().setIfAbsent(lockId, "lock",
                millisecond, TimeUnit.MILLISECONDS);
        return success != null && success;
    }

    // 释放锁
    public void releaseLock(String lockId) {
        getRedisTemplate().delete(lockId);
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(String key) {
        Map<String, T> map = getRedisTemplate().opsForHash().entries(key);
        return map;
    }


    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(String pattern) {
        return getRedisTemplate().keys(pattern);
    }

    public void setCacheObjectIfNotExsit(String key, Object value, int minute) {
        getRedisTemplate().opsForValue().setIfAbsent(key, value, minute, TimeUnit.MINUTES);
    }

    public Long getKeyExpire(String key, TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 模糊查询redis的key
     *
     * @param match
     * @return
     * @throws Exception
     */
    public List<String> scanKeys(String match) throws Exception {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(match).count(1000).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) getRedisTemplate().getKeySerializer();
        Cursor cursor = (Cursor) getRedisTemplate().executeWithStickyConnection(redisConnection ->
                new ConvertingCursor<>(redisConnection.scan(scanOptions), redisSerializer::deserialize));

        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(cursor.next().toString());
        }
        //切记这里一定要关闭，否则会耗尽连接数。报Cannot get Jedis connection
        cursor.close();
        return result;
    }

    /**
     * 批量获取缓存值
     *
     * @param keys
     * @param useParallel
     * @return
     */
    public Map<String, Object> batchQueryByKeys(List<String> keys, Boolean useParallel) {
        if (null == keys || keys.size() == 0) {
            return null;

        }
        if (null == useParallel) {
            useParallel = true;

        }

        List<Object> list = getRedisTemplate().opsForValue().multiGet(keys);

        if (null == list || list.size() == 0) {
            return null;
        }
        Map resultMap = null;
        if (useParallel) {
            Map resultMapOne = Collections.synchronizedMap(new HashMap<>());
            keys.parallelStream().forEach(t -> {
                resultMapOne.put(t, list.get(keys.indexOf(t)));
            });
            resultMap = resultMapOne;
        } else {
            Map resultMapTwo = new HashMap<>();
            for (String t : keys) {
                resultMapTwo.put(t, list.get(keys.indexOf(t)));
            }
            resultMap = resultMapTwo;
        }
        return resultMap;

    }

    public <T> T getAndSet(final String key, T value) {
        T oldValue = null;
        try {
            ValueOperations<String, Object> operations = getRedisTemplate().opsForValue();
            oldValue = (T) operations.getAndSet(key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldValue;
    }


    /***
     * 加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return 锁住返回true
     */
    public boolean lock(String key, String value) {
        key = key + ".lock";
        if (getRedisTemplate().opsForValue().setIfAbsent(key, value, 5, TimeUnit.SECONDS)) {//setNX 返回boolean
            return true;
        }
        //如果锁超时 ***
        String currentValue = (String) getRedisTemplate().opsForValue().get(key);
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldvalue = (String) getRedisTemplate().opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldvalue) && oldvalue.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }

    /***
     * 解锁
     * @param key
     * @param value
     * @return
     */
    public void unlock(String key, String value) {
        key = key + ".lock";
        try {
            String currentValue = (String) getRedisTemplate().opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                getRedisTemplate().opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
