/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.external;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExternalModuleAddon extends AbstractModule {
    @Override
    public String getModuleName() {
        return "external";
    }

    @Override
    public String getLabel() {
        return "扩展模块";
    }

    @Override
    public String getDescription() {
        return "创建模块,修改模块，删除模块等功能";
    }

    @Override
    public Boolean getDisplay() {
        return false;
    }

    @Override
    public void postConstruct() {
        super.postConstruct();
    }
}