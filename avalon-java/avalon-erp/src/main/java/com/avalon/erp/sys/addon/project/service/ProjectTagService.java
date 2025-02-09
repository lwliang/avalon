package com.avalon.erp.sys.addon.project.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectTagService extends AbstractService {
    @Override
    public String getServiceName() {
        return "project.tag";
    }

    @Override
    public String getLabel() {
        return "任务标签";
    }

    public Field color = Fields.createString("颜色");
}
