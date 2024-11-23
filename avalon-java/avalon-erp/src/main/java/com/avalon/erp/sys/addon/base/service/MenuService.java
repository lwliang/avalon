/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.*;
import com.avalon.core.model.Record;
import com.avalon.core.service.AbstractTreeService;
import com.avalon.core.util.FieldValue;
import com.avalon.erp.sys.addon.base.model.enums.ActionTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MenuService extends AbstractTreeService {
    @Override
    public String getServiceName() {
        return "base.menu";
    }

    public final Field label = Fields.createString("显示名称");
    public final Field customUrl = Fields.createString("路由");
    public final Field param = Fields.createString("参数");

    public final Field sequence = Fields.createInteger("序号");
    public final Field action = Fields.createMany2one("动作",
            "base.action.window");
    public final Field objectAction = Fields.createString("对象动作");
    public final Field type = Fields.createSelection("类型", ActionTypeEnum.class, ActionTypeEnum.action);
    public final Field icon = Fields.createString("图标");
    public final Field active = Fields.createBoolean("激活");
    public final Field moduleId = Fields.createMany2one("模块", "base.module");
    public final Field serviceId = Fields.createMany2one("模型", "base.service");
}
