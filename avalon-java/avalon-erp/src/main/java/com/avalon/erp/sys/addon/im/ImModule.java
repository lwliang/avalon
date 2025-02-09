/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.im;

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

    @Override
    public String getIcon() {
        return "resource/im.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/record/base.config.xml",
                "resource/view/im.chat.views.xml",
                "resource/view/menu.xml",
        };
    }

    @Override
    public String[] getStartJS() {
        return new String[]{
                "resource/js/index.js"
        };
    }
}
