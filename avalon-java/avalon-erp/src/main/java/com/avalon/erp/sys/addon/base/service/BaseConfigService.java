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
    public Field label = Fields.createString("标签");

    public Object getConfig(String key) {
        Condition condition = Condition.equalCondition("name", key);
        Record select = select(condition, "id", "value");
        if (!select.isEmpty()) {
            return select.get(0).getRawValue("value");
        }
        return null;
    }

    public void setConfig(String key, String value) {
        Condition condition = Condition.equalCondition("name", key);
        Record select = select(condition, "id", "value");
        if (!select.isEmpty()) {
            RecordRow row = select.get(0);
            row.put("value", value);
            update(row);
        } else {
            RecordRow row = RecordRow.build();
            row.put("key", key);
            row.put("value", value);
            insert(row);
        }
    }
}
