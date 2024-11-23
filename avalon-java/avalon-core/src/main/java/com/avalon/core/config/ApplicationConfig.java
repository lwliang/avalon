/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("application")
@ComponentScan(nameGenerator = ServiceBeanNameGenerator.class)
public class ApplicationConfig {
    private String datetimeFormat = "yyyy-MM-dd HH:mm:ss";//日期时间格式化
    private String dateFormat = "yyyy-MM-dd";//日期格式化
    private String timeFormat = "HH:mm";//时间格式化

    private DataSourceConfig dataSource;

    private Integer pageSize = 80;
    private Boolean debug = false;
}
