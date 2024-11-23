/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.demo;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "demo";
    }

    @Override
    public String getLabel() {
        return "标题";
    }

    @Override
    public String getDescription() {
        return "描述";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }
}
