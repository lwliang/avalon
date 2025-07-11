package com.avalon.erp.sys.addon.email;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/6/19
 */
@Component
@Slf4j
public class EmailModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "email";
    }

    @Override
    public String getLabel() {
        return "邮件";
    }

    @Override
    public String getDescription() {
        return "邮件管理";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/email.png";
    }

    @Override
    public String[] getResource() {
        return new String[] {
                "resource/record/email.server.xml",
                "resource/view/email.server.views.xml",
                "resource/view/email.message.views.xml",
                "resource/view/email.account.views.xml",
                "resource/menu.xml"
        };
    }
}
