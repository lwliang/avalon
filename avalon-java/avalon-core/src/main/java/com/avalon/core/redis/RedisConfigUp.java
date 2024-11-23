/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.redis;

import com.avalon.core.config.RedisConfigProperties;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RedisConfigUp {

    /**
     * Redis多数据库Bean
     */
    @Bean
    public Map<String, Map<String, RedisTemplate<String, Object>>> redisTemplateDatabase(RedisConfigProperties redisConfigProperties) {
        Map<String, Map<String, RedisTemplate<String, Object>>> redisTemplateDatabase = new HashMap<>();
        List<RedisConfigProperties.RedisProperties> redisProperties = redisConfigProperties.getConfig();
        for (RedisConfigProperties.RedisProperties redisPropertiesTemp : redisProperties) {

            Map<String, RedisTemplate<String, Object>> redisTemplateMap = new HashMap<>();

            for (Integer database : redisPropertiesTemp.getDatabase()) {
                // 1. 配置连接
                RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
                redisStandaloneConfiguration.setHostName(redisPropertiesTemp.getHostName());
                redisStandaloneConfiguration.setPort(redisPropertiesTemp.getPort());
                if (redisPropertiesTemp.getUsername() != null && redisPropertiesTemp.getUsername().length() > 0) {
                    redisStandaloneConfiguration.setUsername(redisPropertiesTemp.getUsername());
                }
                if (redisPropertiesTemp.getPassword() != null && redisPropertiesTemp.getPassword().length() > 0) {
                    redisStandaloneConfiguration.setPassword(redisPropertiesTemp.getPassword());
                }
                // 2.配置数据库
                redisStandaloneConfiguration.setDatabase(database);

                // 3.设置连接池
                GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
                LettuceClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
                LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
                lettuceConnectionFactory.afterPropertiesSet();
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(lettuceConnectionFactory);
                // key采用String的序列化方式
                template.setKeySerializer(new StringRedisSerializer());
                // value序列化方式采用jdk
                template.setValueSerializer(new StringRedisSerializer());
                template.afterPropertiesSet();
                // Key 数据库名，value redis操作类
                redisTemplateMap.put(String.valueOf(database), template);
            }
            redisTemplateDatabase.put(redisPropertiesTemp.getKey(), redisTemplateMap);
        }
        return redisTemplateDatabase;
    }
}
