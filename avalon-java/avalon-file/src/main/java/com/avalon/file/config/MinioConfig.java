package com.avalon.file.config;

import com.avalon.file.enums.PathModeEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/23 17:24
 */
@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private PathModeEnums mode = PathModeEnums.date;
}
