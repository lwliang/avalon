/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfigProperties {

    private List<RedisProperties> config = new ArrayList<>();

    @Data
    public static class RedisProperties {
        /**
         * Redis 别名，通过key获取对应连接
         */
        private String key;
        /**
         * Redis 连接地址
         */
        private String hostName;

        /**
         * Redis 连接端口
         */
        private Integer port;

        /**
         * Redis 使用到的数据库
         */
        private Integer[] database;

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;
    }
}