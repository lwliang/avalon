package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.field.*;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/08 22:28
 */
@Service
@Slf4j
public class BaseConfigService extends AbstractService {
    @Override
    public String getServiceName() {
        return "base.config";
    }

    @Override
    protected Field createNameField() {
        return Fields.createString("key", true);
    }

    public Field value = Fields.createString("值");
    public Field moduleId = Fields.createMany2one("模块", "base.module");

    public Object getConfig(String module, String key) {
        Condition condition = Condition.equalCondition("name", key);
        condition = condition.andEqualCondition("moduleId.name", module);
        Record select = select(condition, "id", "value");
        if (ObjectUtils.isNull(select)) {
            return select.get(0).getRawValue("value");
        }
        return null;
    }

    public void setConfig(String module, String key, String type, Object value) {
        Condition condition = Condition.equalCondition("name", key);
        condition = condition.andEqualCondition("moduleId.name", module);
        Record select = select(condition, "id", "value");
        if (!select.isEmpty()) {
            RecordRow row = select.get(0);
            row.put("value", value);
            update(row);
        } else {
            RecordRow row = RecordRow.build();
            row.put("key", key);
            row.put("type", type);
            row.put("value", value);
            insert(row);
        }
    }

    public void setStringConfig(String module, String key, Object value) {
        setConfig(module, key, StringField.class.getSimpleName(), value);
    }

    public void setIntConfig(String module, String key, Object value) {
        setConfig(module, key, IntegerField.class.getSimpleName(), value);
    }

    public void setBooleanConfig(String module, String key, Object value) {
        setConfig(module, key, BooleanField.class.getSimpleName(), value);
    }

    public void setDoubleConfig(String module, String key, Object value) {
        setConfig(module, key, DoubleField.class.getSimpleName(), value);
    }
}
