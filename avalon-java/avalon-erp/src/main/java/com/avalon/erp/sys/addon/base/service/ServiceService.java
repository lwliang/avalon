/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.ExternalService;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import com.avalon.erp.sys.addon.base.model.enums.FieldSourceTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ServiceService extends AbstractService {
    public ServiceService() {
    }

    private ModuleService moduleService;

    public ServiceService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Override
    public String getServiceName() {
        return "base.service";
    }

    protected final Field label = Fields.createString("服务名");
    protected final Field tableName = Fields.createString("表名");
    public final Field moduleId = Fields.createMany2one("模块", "base.module");

    public final Field nameField = Fields.createString("显示字段");

    public final Field keyField = Fields.createString("主键字段");

    public final Field fields = Fields.createOne2many("字段",
            "base.field", "serviceId");

    public final Field sourceType = Fields.createSelection("来源类型", FieldSourceTypeEnum.class,
            FieldSourceTypeEnum.custom);
    public final Field delegateField = Fields.createString("委托继承字段");

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        AbstractService serviceBean = getContext().getServiceBean(recordRow.getString("name"));
        if (ObjectUtils.isNull(serviceBean)) {
            ExternalService externalService = getContext().getNewExternalService();
            externalService.setServiceName(recordRow.getString("name"));
            if (recordRow.containsKey(label)) {
                externalService.setLabel(recordRow.getString("label"));
            }

            FieldValue moduleValue = moduleService.getFieldValue("name",
                    Condition.equalCondition("id", recordRow.getInteger("moduleId")));
            externalService.setModuleName(moduleValue.getString());
            externalService.init();
            getContext().registerSingleton(recordRow.getString("name"), externalService);
            getContext().registerAlias(recordRow.getString("name"), recordRow.getString("name"));
            externalService.upgradeTable();
        } else {
            if (!serviceBean.existTable()) {
                serviceBean.upgradeTable();
            }
        }

        return super.insert(recordRow);
    }

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        return super.update(recordRow);
    }

    @Override
    public Integer delete(RecordRow row) throws AvalonException {
        Integer delete = super.delete(row);
        FieldValue fieldValue = getFieldValue("name",
                Condition.equalCondition("id", row.getInteger("id")));
        getContext().removeSingleton(fieldValue.getString());
        return delete;
    }
}
