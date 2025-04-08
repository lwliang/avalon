/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.config;

import com.avalon.file.enums.PathModeEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "file")
@Component
@Data
public class FileConfig {
    private String file = "./data";
    private String image = "./image";
    private String video = "./video";
    private PathModeEnums mode = PathModeEnums.date;
}
