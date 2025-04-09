package com.avalon.erp.addon.house;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/08 11:24
 */
@Component
@Slf4j
public class HouseModule extends AbstractModule {
    @Override
    public String getModuleName() { // 模块标识 唯一值
        return "house";
    }

    @Override
    public String getLabel() { // 显示标题
        return "租房";
    }

    @Override
    public String getDescription() { // 描述
        return "租房,看房等功能";
    }

    @Override
    public Boolean getDisplay() { // 安装后，显示在左边栏位上
        return true;
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/house.house.views.xml",
                "resource/view/menu.xml",
        };
    }
}
