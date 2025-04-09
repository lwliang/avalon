/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat;

import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.TransientService;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
public class ChatModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "chat";
    }

    @Override
    public String getLabel() {
        return "聊天";
    }

    @Override
    public String getDescription() {
        return "聊天模块";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public void createModule() {
        for (AbstractService service : getServiceList()) {
            if (service instanceof TransientService) {
                continue;
            }
            service.createTable();
        }
    }
}
