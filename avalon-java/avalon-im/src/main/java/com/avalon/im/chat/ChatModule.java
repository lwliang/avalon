/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.im.chat;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
}
