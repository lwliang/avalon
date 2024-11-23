/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.external.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.module.ExternalModule;
import com.avalon.erp.sys.addon.base.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalModuleService extends ModuleService {
    @Override
    public String getServiceName() {
        return "external.module";
    }

    private final Field isExternal = Fields.createBoolean("扩展", false, true);

    /**
     * 获取所有扩张模块模块
     *
     * @return 模块列表
     */
    public Record getAllExternalModes() {
        return select("moduleName, label, description, display, icon, isEnable, isExternal",
                Condition.equalCondition(isExternal, true));
    }


    /**
     * 注册模块
     *
     * @param row 模块
     */
    public ExternalModule register(RecordRow row) {
        Context context = getContext();
        ExternalModule module = context.getAvalonApplicationContext().getBean(ExternalModule.class);
        module.setModuleName(row.getString("name"));
        module.setLabel(row.getString("label"));
        module.setDescription(row.getString("description"));
        module.setIcon(row.getString("icon"));
        module.setIsExternal(row.getBoolean("isExternal"));
        module.setIsInstall(row.getBoolean("isInstall"));
        module.setDisplay(row.getBoolean("display"));
        module.register();
        return module;
    }
}
