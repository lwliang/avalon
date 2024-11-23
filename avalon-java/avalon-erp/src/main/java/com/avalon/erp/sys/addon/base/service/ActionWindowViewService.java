/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.base.model.enums.ViewModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActionWindowViewService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.action.window.view";
    }

    protected final Field label = Fields.createString("名称");
    protected final Field serviceName = Fields.createString("模型名称");
    protected final Field viewMode = Fields.createSelection("类型", ViewModeEnum.class, ViewModeEnum.tree);
    protected final Field arch = Fields.createText("xml");

    public final Field actionWindowId = Fields.createMany2one("窗口", "base.action.window");
}
