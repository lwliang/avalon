/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.im;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "im";
    }

    @Override
    public String getLabel() {
        return "im模块";
    }

    @Override
    public String getDescription() {
        return "im模块";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }
}
