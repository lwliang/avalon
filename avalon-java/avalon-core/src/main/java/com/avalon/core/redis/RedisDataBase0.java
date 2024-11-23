/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Primary
public class RedisDataBase0 extends RedisCommon {

    private final RedisUtils redisUtils;

    public RedisDataBase0(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @Override
    protected RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisUtils.getRedisTemplate("redis-0", "0");
    }
}
