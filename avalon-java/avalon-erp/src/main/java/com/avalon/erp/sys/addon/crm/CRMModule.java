package com.avalon.erp.sys.addon.crm;

import com.avalon.core.module.AbstractModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/11 19:12
 */
@Service
@Slf4j
public class CRMModule extends AbstractModule {
    @Override
    public String getModuleName() {
        return "crm";
    }

    @Override
    public String getLabel() {
        return "CRM";
    }

    @Override
    public String getDescription() {
        return "客户关系管理依托于持续收集和分析客户数据、然后利用这些洞察来深化客户关系和改善业务成效";
    }

    @Override
    public Boolean getDisplay() {
        return true;
    }

    @Override
    public String getIcon() {
        return "resource/crm.png";
    }

    @Override
    public String[] depends() {
        return new String[]{"crm.common"};
    }

    @Override
    public String[] getResource() {
        return new String[]{
                "resource/view/menu.xml",
        };
    }
}
