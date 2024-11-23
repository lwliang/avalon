/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.config;

import com.avalon.core.util.StringUtils;
import lombok.Data;

@Data
public class DataSourceConfig {
    private String host;
    private Integer port;
    private String classType;
    private String username;
    private String password;
    private String timezone;
    private String database;
    private Integer maxPoolSize;
    private Integer minIdle;
    private Integer connectionTimeout;
    private Integer idleTimeout;
    private Integer maxLifetime;

    public Boolean isMysql() {
        return StringUtils.isNotEmpty(classType) && classType.contains("mysql");
    }

    public Boolean isPostgres() {
        if (StringUtils.isEmpty(classType)) {
            return true;
        }
        return classType.contains("postgres");
    }
}
