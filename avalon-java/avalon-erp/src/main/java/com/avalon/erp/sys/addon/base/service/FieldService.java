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
public class FieldService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.field";
    }

    protected final Field label = Fields.createString("字段名");
    protected final Field isPrimaryKey = Fields.createBoolean("主键");
    protected final Field isAutoIncrement = Fields.createBoolean("自增");
    protected final Field isRequired = Fields.createBoolean("必填");
    protected final Field isReadonly = Fields.createBoolean("只读");
    protected final Field defaultValue = Fields.createString("默认值");
    protected final Field type = Fields.createFieldSelectionField();
    protected final Field serviceId = Fields.createMany2one("服务", "base.service");
    protected final Field isUnique = Fields.createBoolean("唯一");
    protected final Field allowNull = Fields.createBoolean("允许空");
    protected final Field isMulti = Fields.createBoolean("多选");
    protected final Field minValue = Fields.createBigDecimal("最小值", 50, 6);
    protected final Field maxValue = Fields.createBigDecimal("最大值", 50, 6);

    public final Field masterForeignKeyName = Fields.createString("主外键");
    public final Field relativeForeignKeyName = Fields.createString("关联表外键");
    public final Field relativeServiceName = Fields.createString("关联服务");
    public final Field manyServiceTable = Fields.createString("多对多关联服务");
    public final Field relativeFieldName = Fields.createString("关联字段");
}
