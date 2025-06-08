package com.avalon.erp.sys.addon.common;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 19:12
 */
@Service
@Slf4j
public class CommonModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "common";
    }

    @Override
    public String getLabel() {
        return "业务基础数据";
    }

    @Override
    public String getDescription() {
        return "基础数据";
    }

    @Override
    public Boolean getDisplay() {
        return false;
    }

    @Override
    public String getIcon() {
        return "resource/crm.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/partner.views.xml"
        };
    }
}
