/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.addon.bank;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BankModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "bank";
    }

    @Override
    public String getLabel() {
        return "银行";
    }

    @Override
    public String getDescription() {
        return "对接农业银行";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/bank.png";
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/bank.school.views.xml",
                "resource/view/bank.school.course.views.xml",
                "resource/view/menus.xml"
        };
    }
}
