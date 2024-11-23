/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.base.model.enums.ViewModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ActionViewService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.action.view";
    }

    @Override
    public String getLabel() {
        return "视图";
    }

    public final Field serviceId = Fields.createMany2one("服务", "base.service");
    public final Field viewMode = Fields.createSelection("类型", ViewModeEnum.class, ViewModeEnum.tree);
    public final Field label = Fields.createString("名称");
    public final Field priority = Fields.createInteger("优先级", 16);

    public final Field arch = Fields.createHtml("视图定义");

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        return super.update(recordRow);
    }
}
