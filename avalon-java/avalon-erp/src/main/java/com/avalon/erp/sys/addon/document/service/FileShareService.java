package com.avalon.erp.sys.addon.document.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileShareService extends AbstractService {
    @Override
    public String getServiceName() {
        return "document.file.share";
    }

    @Override
    public String getLabel() {
        return "文件共享";
    }

    public Field fileId = Fields.createMany2one("文件", "document.file");
    public Field shareId = Fields.createMany2one("共享者", "base.user");
    public Field userIds = Fields.createOne2many("协同者", "document.file.share.user");
}
