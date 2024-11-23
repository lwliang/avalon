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
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IServiceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseServiceDataService extends AbstractService implements IServiceDataService {
    @Override
    public String getServiceName() {
        return "base.service.data";
    }

    @Override
    public String getLabel() {
        return "基础数据";
    }

    public final Field moduleId = Fields.createMany2one("模块", "base.module");
    public final Field serviceId = Fields.createMany2one("服务", "base.service");
    public final Field sourceId = Fields.createInteger("来源ID", true);

    @Override
    public void insert(String moduleName, Integer dstServiceId,
                       String id, String serviceName, RecordRow recordRow) {
        AbstractService serviceBean = getContext().getServiceBean(serviceName);
        PrimaryKey key = serviceBean.insert(recordRow);

        AbstractService moduleService = getContext().getServiceBean("base.module");
        Record moduleRecord = moduleService.search(Condition.equalCondition("name",
                moduleName));
        Integer moduleId = moduleRecord.get(0).getInteger("id");

        RecordRow row = RecordRow.build()
                .put("name", id)
                .put(this.moduleId, moduleId)
                .put(this.serviceId, dstServiceId)
                .put(sourceId, key.getInteger());

        insert(row);
    }

    @Override
    public void update(String serviceName, RecordRow recordRow) {
        AbstractService serviceBean = getContext().getServiceBean(serviceName);
        serviceBean.update(recordRow);
    }

    @Override
    public Integer refId(String moduleName, String id) {
        Condition condition = Condition.equalCondition("name", id);
        condition = condition.andEqualCondition("moduleId.name", moduleName);

        Record select = select(condition, "sourceId");
        if (select.isEmpty()) {
            return null;
        }
        return select.get(0).getInteger("sourceId");
    }

    @Override
    public Integer refId(String id) {
        Condition condition = Condition.equalCondition("name", id);

        Record select = select(condition, "id");
        if (select.isEmpty()) {
            return null;
        }
        return select.get(0).getInteger("id");
    }
}