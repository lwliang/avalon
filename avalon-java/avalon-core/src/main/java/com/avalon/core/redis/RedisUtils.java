/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisUtils {

    private final Map<String, Map<String, RedisTemplate<String, Object>>> redisTemplateDatabase;

    /**
     * 放入Redis
     *
     * @param redisKey 对应Redis连接Key
     * @param database 对应数据库
     * @param key      放入的Key
     * @param Value    放入的Value
     */
    public void set(String redisKey, String database, String key, Object Value) {
        Map<String, RedisTemplate<String, Object>> stringRedisTemplateMap = redisTemplateDatabase.get(redisKey);
        stringRedisTemplateMap.get(database).opsForValue().set(key, Value);
    }

    /**
     * 取出Redis对应Redis值
     *
     * @param redisKey 对应Redis连接Key
     * @param database 对应数据库
     * @param key      取值的Key
     * @return key对应的Value
     */
    public Object get(String redisKey, String database, String key) {
        Map<String, RedisTemplate<String, Object>> stringRedisTemplateMap = redisTemplateDatabase.get(redisKey);
        return stringRedisTemplateMap.get(database).opsForValue().get(key);
    }

    /**
     * 返回数据库的链接
     *
     * @param redisKey
     * @param database
     * @return
     */
    public RedisTemplate<String, Object> getRedisTemplate(String redisKey, String database) {
        Map<String, RedisTemplate<String, Object>> stringRedisTemplateMap = redisTemplateDatabase.get(redisKey);
        return stringRedisTemplateMap.get(database);
    }

    public RedisUtils(Map<String, Map<String, RedisTemplate<String, Object>>> redisTemplateDatabase) {
        this.redisTemplateDatabase = redisTemplateDatabase;
    }



}
