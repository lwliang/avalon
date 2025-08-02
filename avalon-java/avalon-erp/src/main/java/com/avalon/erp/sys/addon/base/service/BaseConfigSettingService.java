package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.RecordRow;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.TransientService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseConfigSettingService extends TransientService {
    @Override
    public String getServiceName() {
        return "base.config.setting";
    }

    @Override
    public RecordRow create(RecordRow defaultRow) throws AvalonException {
        RecordRow initRow = RecordRow.build();
        BaseConfigService configService = (BaseConfigService) getServiceBean("base.config");
        initRow.put("theme", configService.getConfig("base.config.theme"));
        return initRow;
    }

    @Override
    public String getLabel() {
        return "设置";
    }

    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (recordRow.containsKey("theme")) {
            invokeMethod("base.config", "setConfig", "base.config.theme", recordRow.getString("theme"));
        }
        return super.insert(recordRow);
    }

    public Field theme = Fields.createSelection("主题", new SelectionHashMap() {
        {
            put("white", "白色");
            put("black", "黑色");
        }
    });
}
