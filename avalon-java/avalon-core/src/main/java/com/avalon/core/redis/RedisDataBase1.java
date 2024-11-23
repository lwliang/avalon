/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisDataBase1 extends RedisCommon {

    private final RedisUtils redisUtils;

    public RedisDataBase1(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisUtils.getRedisTemplate("redis-0", "1");
    }
}