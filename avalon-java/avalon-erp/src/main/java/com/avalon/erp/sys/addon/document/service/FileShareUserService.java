package com.avalon.erp.sys.addon.document.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileShareUserService extends AbstractService {
    @Override
    public String getServiceName() {
        return "document.file.share.user";
    }

    @Override
    public String getLabel() {
        return "文件共享用户";
    }

    public Field shareId = Fields.createMany2one("共享用户", "base.user");
    public Field permission = Fields.createSelection("权限", new SelectionHashMap() {{
        put("read", "读");
        put("write", "修改");
        put("download", "下载");
        put("delete", "删除");
    }}, "read", true);

}
