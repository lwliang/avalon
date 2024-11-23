/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "file";
    }

    @Override
    public String getLabel() {
        return "文件管理";
    }

    @Override
    public String getDescription() {
        return "文件管理";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }
}
