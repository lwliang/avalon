/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.service";
    }

    protected final Field label = Fields.createString("服务名");
    protected final Field tableName = Fields.createString("表名");
    protected final Field moduleId = Fields.createMany2one("模块", "base.module");

    public final Field nameField = Fields.createString("显示字段");

    public final Field keyField = Fields.createString("主键字段");
}
